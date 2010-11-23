package nu.ted;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import nu.ted.domain.TedBackendWrapper;
import nu.ted.generated.Series;
import nu.ted.generated.Ted;
import nu.ted.generated.TedConfig;
import nu.ted.generated.TedService;
import nu.ted.generated.TorrentSource;
import nu.ted.generated.UidCache;
import nu.ted.guide.GuideDB;
import nu.ted.guide.GuideFactory;
import nu.ted.guide.tvdb.TVDB;
import nu.ted.guide.tvdb.datasource.CacheDataSource;
import nu.ted.guide.tvdb.datasource.DirectDataSource;
import nu.ted.guide.tvrage.TVRage;
import nu.ted.service.TedServiceImpl;
import nu.ted.thrift.TedServerSecureSocket;
import nu.ted.thrift.TThreadPoolServer;
import nu.ted.torrent.search.Rss;
import nu.ted.torrent.search.TorrentSourceIndex;
import nu.ted.www.DirectPageLoader;

import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jordanzimmerman.SRPFactory;
import com.jordanzimmerman.SRPVerifier;
import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;

public class Server {

	private final Logger logger = LoggerFactory.getLogger(Server.class);

	private static TedServiceImpl service;
	private static Ted ted;

	private static Boolean secure = true;
	private static boolean overridePassword = false;
	private static String verifier;
	private static String salt;

	private static void writeFileFromBytes(final File file, final byte[] data) throws IOException {
		if (!file.exists()) {
			if (!file.createNewFile()) {
				throw new IOException("Problem creating file: " + file.getAbsolutePath());
			}
		}

		if (!file.isFile()) {
			throw new IOException("Cannot write to non-file: " + file.getAbsolutePath());
		}

		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			throw new IOException("File not found", e);
		}
		out.write(data);
		out.close();
	}

	private static byte[] readFileToBytes(final File file) throws IOException {

		if (file.isDirectory())
			throw new RuntimeException("Unsupported operation, file "
					+ file.getAbsolutePath() + " is a directory");
		if (file.length() > Integer.MAX_VALUE)
			throw new RuntimeException("Unsupported operation, file "
					+ file.getAbsolutePath() + " is too big");

		FileInputStream in = new FileInputStream(file);
		final byte bytes[] = new byte[(int) file.length()];

		try {
			// Read in the bytes
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
					&& (numRead = in.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}

			// Ensure all the bytes have been read in
			if (offset < bytes.length) {
				throw new IOException("Could not completely read file " + file.getName());
			}
		} finally {
			// Close the input stream and return bytes
			in.close();
		}
		return bytes;
	}

	/*
	 * This should only be used to set default values, and not actually do anything with them.
	 * This is because all or some of the values may be replaced with the saved copy.
	 */
	public static Ted createDefaultTed()
	{
		Ted ted = new Ted();

		TedConfig config = new TedConfig();

		// Setup default config.
		config.setTorrentSources(new LinkedList<TorrentSource>());

		File saveLocation = new File(System.getProperty("user.dir") + "/download/");
		config.setSavePath(saveLocation.getPath());

		// TODO: will probably want to create some default TorrentSources around here or in TedConfig()
		ted.setConfig(config);
		ted.setSeries(new LinkedList<Series>());
		ted.setSeriesUidCache(new UidCache());
		return ted;
	}

	public static byte[] serializeTed(Ted ted)
	{
		TSerializer serializer = new TSerializer(new TJSONProtocol.Factory());
		try {
			return serializer.serialize(ted);
		} catch (TException e) {
			// This should never happen.
			throw new RuntimeException("Unable to serialize ted object.", e);
		}
	}

	public static Ted deserializeTed(byte[] bytes)
	{
		// Use the default to missing values may be filled in.
		Ted ted = createDefaultTed();
		TDeserializer deserializer = new TDeserializer(new TJSONProtocol.Factory());
		try {
			deserializer.deserialize(ted, bytes);
		} catch (TException e) {
			// TODO: checked exception
			throw new RuntimeException("Unable to deserialize ted bytes.", e);
		}

		return ted;
	}

	private void start(String fileLocation)
	{
		Ted ted = createDefaultTed();
		File dataFile = new File(fileLocation);
		if (dataFile.exists()) {
			try {
				ted = deserializeTed(readFileToBytes(dataFile));
			} catch (IOException e) {
				// TODO: we're ignoring bad files here, which could result in the file
				// being overwritten with default settings.
				logger.warn("Error reading data file.", e);
			}
		}
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(
				new TedSaver(fileLocation), 30, 90, TimeUnit.SECONDS);
		start(ted);
	}

	private void start()
	{
		logger.warn("Data will not be persisted. See '-h'");
		start(createDefaultTed());
	}

	private static class TedSaver implements Runnable {
		private String location;

		public TedSaver(String location) {
			this.location = location;
		}

		@Override
		public void run() {
			byte[] data = serializeTed(ted);
			try {
				Server.writeFileFromBytes(new File(location), data);
				Server.writeFileFromBytes(new File(location + ".backup"), data);
			} catch (IOException e) {
				throw new RuntimeException("Unable to save file", e);
			}
		}

	}

	private static class EpisodeUpdater implements Runnable {

		public void run() {
			// TODO: subscribe to events for new series, and changes to series.
			try {
				if (new TedBackendWrapper(ted).updateSeries(Calendar.getInstance()) == true) {
					Searcher.scheduleRun();
				}

			} catch (Throwable ex) {
				LoggerFactory.getLogger(Server.class).warn("EpisodeUpdater crashed", ex);
			}
		}
	}

	private void start(Ted ted)
	{
		try
		{
			Server.ted = ted;
			TedConfig config = ted.getConfig();

			TServerTransport serverTransport;

			String verifier;
			String salt;
			if (overridePassword) {
				verifier = Server.verifier;
				salt = Server.salt;
			} else {
				verifier = config.getVerifier();
				salt = config.getSalt();

			}
			if (secure && verifier != null && verifier.length() > 0 &&
					salt != null && salt.length() > 0) {
				serverTransport = new TedServerSecureSocket(config.getPort(),
						verifier, salt);
			} else {
				logger.warn("Running unencrypted protocol.");
				serverTransport = new TServerSocket(config.getPort());
			}

			// Setup Guide
			try {
				GuideFactory.addGuide(new TVDB(
						new CacheDataSource(new DirectDataSource(new DirectPageLoader()))));
			} catch (DataTransferException e) {
				// For now bring it all down. If we have no TVDB we're likely sunk.
				throw new RuntimeException("Unable to connect to TVDB", e);
			}
			GuideFactory.addGuide(new TVRage());

			GuideDB guide = GuideFactory.getGuide(TVDB.NAME);
			service = new TedServiceImpl(ted, guide);

			// Setup Episode Searchers
			TorrentSourceIndex.registerFactory(Rss.name, new Rss.RssFactory());

			// Setup download directory
			File saveLocation = new File(config.getSavePath());
			if (!saveLocation.exists()) {
				logger.info("Creating download location {}", saveLocation);
				if (saveLocation.mkdirs() == false) {
					logger.warn("Unable to create location {}", saveLocation);
					System.exit(-1);
				}
			}

			// Setup the worker thread:
			ScheduledExecutorService searcherExecutor = Executors.newScheduledThreadPool(1);
			Searcher.setup(searcherExecutor, ted);

			if (new TedBackendWrapper(ted).hasMissingEpisodes() == true) {
				searcherExecutor.schedule(new Searcher(), 20, TimeUnit.SECONDS);
			}
			searcherExecutor.scheduleAtFixedRate(new EpisodeUpdater(), 10, 3600, TimeUnit.SECONDS);

			// Create an executor service that will always call the logout
			// after the client disconnects.
			ExecutorService executor = new ThreadPoolExecutor(5, Integer.MAX_VALUE,
					60,
					TimeUnit.SECONDS,
					new SynchronousQueue<Runnable>()) {

						@Override
						protected void afterExecute(Runnable r, Throwable t) {
							super.afterExecute(r, t);
							try {
								service.logout();
							} catch (TException e) {
								throw new RuntimeException("Error logging out thread", e);
							}
						}

			};

			// TODO: Dependency Injection?
			TedService.Processor processor = new TedService.Processor(service);
			Factory protFactory = new TBinaryProtocol.Factory(true, true);
			TServer server = new TThreadPoolServer(processor, serverTransport, protFactory,
					executor);
			logger.info("Starting server on port {} ...", config.getPort());
			server.serve();
		}
		catch (TTransportException e)
		{
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private static String createErrorMessage(JSAPResult config) {
		StringBuffer result = new StringBuffer();
		for (Iterator<String> i = config.getErrorMessageIterator(); i.hasNext();){
			result.append("Error: " + i.next() + "\n");
		}
		return result.toString();
	}

	public static void main(String args[]) throws Exception {

		Server srv = new Server();
		if (args.length == 0) {
			srv.start();
			return;
		}

		FlaggedOption data = new FlaggedOption("data");
		data.setShortFlag('d');
		data.setLongFlag("data");
		data.setHelp("Location for persisted data file");

		Switch help = new Switch("help");
		help.setShortFlag('h');
		help.setLongFlag("help");
		help.setHelp("Show this page");

		Switch insecure = new Switch("Insecure");
		insecure.setShortFlag('v');
		insecure.setLongFlag("insecure");
		insecure.setHelp("Run unencrypted protocol");

		FlaggedOption password = new FlaggedOption("password");
		password.setShortFlag('p');
		password.setLongFlag("password");
		password.setHelp("Set password (insecure)");

		JSAP jsap = new JSAP();
		jsap.registerParameter(data);
		jsap.registerParameter(help);
		jsap.registerParameter(insecure);
		jsap.registerParameter(password);

		JSAPResult config = jsap.parse(args);

		// Check this one first, then later errors will be ignored:
		if (config.getBoolean("help")) {
			System.err.println("Usage: java " + Server.class.getName());
			System.err.println(jsap.getHelp());
			System.exit(0);
		}

		if (!config.success()) {
			System.err.println();

			String errors = createErrorMessage(config);
			if (errors.length() > 0) {
				System.err.println(errors);
			}

			System.err.println("Usage: java " + Server.class.getName() + jsap.getUsage());
			System.err.println();
			System.err.println(jsap.getHelp());
			System.exit(1);
		}

		if (config.contains("insecure") && config.getBoolean("insecure")) {
			secure = false;
		} else if (config.contains("password")) {
			String pw = config.getString("password");

			SRPVerifier srpVerifier = SRPFactory.getInstance().makeVerifier(pw.getBytes());

			overridePassword = true;
			verifier = srpVerifier.verifier_v.toString(16);
			salt = srpVerifier.salt_s.toString(16);
		}

		if (!config.contains("data")) {
			srv.start();
		} else {
			srv.start(config.getString("data"));
		}
	}
}
