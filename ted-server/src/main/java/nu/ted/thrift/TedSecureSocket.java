package nu.ted.thrift;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;

import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import com.jordanzimmerman.SRPClientSessionRunner;
import com.jordanzimmerman.SRPFactory;
import com.jordanzimmerman.SRPInputStream;
import com.jordanzimmerman.SRPOutputStream;
import com.jordanzimmerman.SRPServerSessionRunner;
import com.jordanzimmerman.SRPVerifier;

public class TedSecureSocket extends TSocket {

	/**
	 * From the server side, you should not store the password, so pass v & s
	 */
	public TedSecureSocket(Socket socket, String verifier, String salt) throws TTransportException {
		super(socket);

		final SRPVerifier srpVerifier = new SRPVerifier(new BigInteger(verifier, 16), new BigInteger(salt, 16));

		SRPInputStream in = new SRPInputStream(inputStream_);
		SRPOutputStream out = new SRPOutputStream(outputStream_);

		SRPServerSessionRunner runner =
			new SRPServerSessionRunner(SRPFactory.getInstance().newServerSession(srpVerifier));

		// important - both streams must be authenticated
		try {
			in.authenticate(runner, out);
			out.authenticate(runner, in);
		} catch (IOException e) {
			close();
			throw new TTransportException(TTransportException.NOT_OPEN, e);
		}

		inputStream_ = in;
		outputStream_ = out;
	}

	/**
	 * From the client side, you should only have the password accepted from the client.
	 */
	public TedSecureSocket(Socket socket, String password) throws TTransportException {
		super(socket);

		SRPInputStream in = new SRPInputStream(inputStream_);
		SRPOutputStream out = new SRPOutputStream(outputStream_);

		SRPClientSessionRunner runner =
			new SRPClientSessionRunner(SRPFactory.getInstance().newClientSession(password.getBytes()));

		try {
			in.authenticate(runner, out);
			out.authenticate(runner, in);
		} catch (IOException e) {
			close();
			throw new TTransportException(TTransportException.NOT_OPEN, e);
		}

		inputStream_ = in;
		outputStream_ = out;
	}

	// SRPVerifier verifier = factory.makeVerifier(password.getBytes());
	// System.out.println("v: " + verifier.verifier_v.toString(16));
	// System.out.println("s: " + verifier.salt_s.toString(16));
}
