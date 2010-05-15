package nu.ted.gwt.server.page.search;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import net.bugsquat.diservlet.ImageServlet;
import net.bugsquat.diservlet.ImageStore;
import nu.ted.generated.TedService.Client;
import nu.ted.generated.TedService.Iface;
import nu.ted.thrift.TedSecureSocket;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TedRemoteServiceServlet extends RemoteServiceServlet {

	private static final long serialVersionUID = 1L;
	// private static final String CONNECTION_ATTR_NAME = "tedConnection";

	private static boolean secure = false;

	private static Iface client;
	private static TTransport transport;

	public synchronized Iface getTedClient() throws TTransportException {

		if (client == null || !transport.isOpen()) {

			TTransport transport;
			if (secure) {
				Socket socket;
				try {
					socket = new Socket("localhost", 9030);
				} catch (UnknownHostException e) {
					throw new TTransportException(e);
				} catch (IOException e) {
					throw new TTransportException(e);
				}
				transport = new TedSecureSocket(socket, "password");
			} else {
				transport = new TSocket("localhost", 9030);
				transport.open();
			}
			TProtocol protocol = new TBinaryProtocol(transport);
			client = new Client(protocol);
			TedRemoteServiceServlet.transport = transport;
		}
		return client;
	}

	// TODO: I'd like to store this is the session, but I'm not sure
	// how that works with GWT and these servlets. If it would be added
	// by one then used by another this probably wouldn't work. Perhaps
	// we need a static object to track the client connection?
//		Iface client;
//		HttpSession session = getThreadLocalRequest().getSession(true);
//		synchronized (session) {
//			client = (Iface) session.getAttribute(CONNECTION_ATTR_NAME);
//
//			if (client == null) {
//				TTransport transport = new TSocket("localhost", 9030);
//				TProtocol protocol = new TBinaryProtocol(transport);
//				transport.open();
//				client = new Client(protocol);
//				session.setAttribute(CONNECTION_ATTR_NAME, client);
//			}
//		}
//
//		return client;
// 	}

	// TODO: This may be used to call methods on a different servlet. Apparently
	// this is bad and we should be using a RequestDispatcher instead. Should look
	// into that as a solution.
	protected ImageStore getImageStore() {
		return (ImageStore) getServletContext().getAttribute(ImageServlet.IMAGE_STORE_ATTR_KEY);
	}

}
