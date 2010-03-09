package nu.ted;

import nu.ted.gen.TedService;
import nu.ted.guide.tvdb.TVDB;
import nu.ted.service.TedServiceImpl;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;


public class Server
{

	private void start()
	{
		try
		{
			TServerSocket serverTransport = new TServerSocket(9030);
			
			// TODO: Dependency Injection?
			TedService.Processor processor = new TedService.Processor(new TedServiceImpl(new TVDB()));
			Factory protFactory = new TBinaryProtocol.Factory(true, true);
			TServer server = new TThreadPoolServer(processor, serverTransport, protFactory);
			System.out.println("Starting server on port 9030 ...");
			server.serve();
		}
		catch (TTransportException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String args[])
	{
		Server srv = new Server();
		srv.start();
	}

}
