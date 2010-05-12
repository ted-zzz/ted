package nu.ted.thrift;

import java.net.Socket;

import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

/**
 * Extend the modified TServerSocket to return a secure socket
 *
 * Could super() a bindaddr if we need to listen on one interface.
 *
 */
public class TedServerSecureSocket extends TedServerSocket {

	String verifier;
	String salt;

	public TedServerSecureSocket(int port, String verifier, String salt) throws TTransportException {
		super(port);
		this.verifier = verifier;
		this.salt = salt;
	}

	@Override
	protected TSocket createTSocket(Socket socket) throws TTransportException {
		return new TedSecureSocket(socket, verifier, salt);
	}

}
