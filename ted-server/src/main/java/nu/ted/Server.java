package nu.ted;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import nu.ted.domain.TedBackendWrapper;
import nu.ted.generated.Series;
import nu.ted.generated.Ted;
import nu.ted.generated.TedConfig;
import nu.ted.generated.TedService;
import nu.ted.guide.DataTransferException;
import nu.ted.guide.GuideDB;
import nu.ted.guide.GuideFactory;
import nu.ted.guide.tvdb.TVDB;
import nu.ted.guide.tvdb.datasource.direct.DirectDataSource;
import nu.ted.guide.tvrage.TVRage;
import nu.ted.service.TedServiceImpl;
import nu.ted.thrift.TedServerSecureSocket;
import nu.ted.thrift.TThreadPoolServer;
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

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;

public class Server {

	private static TedServiceImpl service;
	private static Ted ted;
	private static Boolean secure = true;

	private byte[] readFileToBytes(final File file) throws IOException {

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

	public static Ted createDefaultTed()
	{
		Ted ted = new Ted();
		ted.setConfig(new TedConfig());
		ted.setSeries(new LinkedList<Series>());
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
				System.err.println("Error reading data file.");
				e.printStackTrace();
			}
		}
		start(ted);
	}

	private void start()
	{
		System.err.println("Warning: Data will not be persisted. See '-h'.");
		start(createDefaultTed());
	}

	private static class EpisodeUpdater implements Runnable {
		public void run() {
			try {
				new TedBackendWrapper(ted).updateSeries(Calendar.getInstance());
			} catch (Throwable ex) {
				System.err.println("EpisodeUpdater crashed: ");
				ex.printStackTrace();
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

			String verifier = config.getVerifier();
			String salt = config.getSalt();
			if (secure && verifier != null && verifier.length() > 0 &&
					salt != null && salt.length() > 0) {
				serverTransport = new TedServerSecureSocket(config.getPort(),
						config.getVerifier(), config.getSalt());
			} else {
				System.err.println("Warning: Running unencrypted protocol.");
				serverTransport = new TServerSocket(config.getPort());
			}

			try {
				GuideFactory.addGuide(new TVDB(new DirectDataSource(new DirectPageLoader())));
			} catch (DataTransferException e) {
				// For now bring it all down. If we have no TVDB we're likely sunk.
				throw new RuntimeException("Unable to connect to TVDB", e);
			}
			GuideFactory.addGuide(new TVRage());

			GuideDB guide = GuideFactory.getGuide(TVDB.NAME);
			service = new TedServiceImpl(ted, guide);

			Executors.newScheduledThreadPool(1).scheduleAtFixedRate(
					new EpisodeUpdater(), 10, 3600, TimeUnit.SECONDS);

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
			System.out.println("Starting server on port " + config.getPort() + " ...");
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

		JSAP jsap = new JSAP();
		jsap.registerParameter(data);
		jsap.registerParameter(help);
		jsap.registerParameter(insecure);

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

		if (config.getBoolean("insecure")) {
			secure = false;
		}

		if (!config.contains("data")) {
			srv.start();
		} else {
			srv.start(config.getString("data"));
		}
	}
}
