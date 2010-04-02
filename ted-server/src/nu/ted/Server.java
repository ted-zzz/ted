package nu.ted;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;

import nu.ted.generated.Constants;
import nu.ted.generated.Series;
import nu.ted.generated.Ted;
import nu.ted.generated.TedConfig;
import nu.ted.generated.TedService;
import nu.ted.guide.GuideDB;
import nu.ted.guide.GuideFactory;
import nu.ted.guide.tvdb.TVDB;
import nu.ted.guide.tvrage.TVRage;
import nu.ted.service.TedServiceImpl;

import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;

public class Server {

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

	private void start(String fileLocation)
	{
		Ted ted = createDefaultTed();

		try
		{
			File dataFile = new File(fileLocation);
			if (dataFile.exists()) {
				TDeserializer deserializer = new TDeserializer(new TJSONProtocol.Factory());

				try {
					byte[] dataFileBytes = readFileToBytes(dataFile);
					deserializer.deserialize(ted, dataFileBytes);
				} catch (IOException e) {
					System.err.println("Error reading data file.");
					e.printStackTrace();
				} catch (TException e) {
					System.err.println("Unable to parse data file.");
					e.printStackTrace();
				}

			}
			TedConfig config = ted.getConfig();

			TServerSocket serverTransport = new TServerSocket(config.getPort());

			GuideFactory.addGuide(new TVDB());
			GuideFactory.addGuide(new TVRage());

			GuideDB guide = GuideFactory.getGuide(TVDB.NAME);
			TedServiceImpl service = new TedServiceImpl(ted, guide);

			// TODO: Dependency Injection?
			TedService.Processor processor = new TedService.Processor(service);
			Factory protFactory = new TBinaryProtocol.Factory(true, true);
			TServer server = new TThreadPoolServer(processor, serverTransport, protFactory);
			System.out.println("Starting server on port 9030 ...");
			server.serve();
		}
		catch (TTransportException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static void main(String args[]) throws Exception {
		FlaggedOption data = new FlaggedOption("data");
		data.setShortFlag('d');
		data.setLongFlag("data");
		data.setRequired(true);
		data.setHelp("Location for persisted data file");

		JSAP jsap = new JSAP();
		jsap.registerParameter(data);

		JSAPResult config = jsap.parse(args);
		if (config.success()) {
			System.err.println("Usage: java " + Server.class.getName()
					+ jsap.getHelp());
			System.exit(1);
		}

		Server srv = new Server();
		srv.start(config.getString("data"));
	}

}
