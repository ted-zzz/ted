/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package nu.ted.thrift;

import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Wrapper around a wrapper around ServerSocket for Thrift.
 *
 * Adds createTSocket and has 'result2 =' use it.
 * Replace all TServerSocket with TedServerSocket.
 * Adds need imports.
 */
public class TedServerSocket extends TServerTransport {

private static final Logger LOGGER = LoggerFactory.getLogger(TedServerSocket.class.getName());

/**
   * Underlying serversocket object
   */
private ServerSocket serverSocket_ = null;

/**
   * Port to listen on
   */
private int port_ = 0;

/**
   * Timeout for client sockets from accept
   */
private int clientTimeout_ = 0;

/**
   * Creates a server socket from underlying socket object
   */
public TedServerSocket(ServerSocket serverSocket) {
	this(serverSocket, 0);
}

/**
   * Creates a server socket from underlying socket object
   */
public TedServerSocket(ServerSocket serverSocket, int clientTimeout) {
	serverSocket_ = serverSocket;
	clientTimeout_ = clientTimeout;
}

/**
   * Creates just a port listening server socket
   */
public TedServerSocket(int port) throws TTransportException {
	this(port, 0);
}

/**
   * Creates just a port listening server socket
   */
public TedServerSocket(int port, int clientTimeout) throws TTransportException {
	this(new InetSocketAddress(port), clientTimeout);
	port_ = port;
}

public TedServerSocket(InetSocketAddress bindAddr) throws TTransportException {
	this(bindAddr, 0);
}

public TedServerSocket(InetSocketAddress bindAddr, int clientTimeout) throws TTransportException {
	clientTimeout_ = clientTimeout;
	try {
	// Make server socket
	serverSocket_ = new ServerSocket();
	// Prevent 2MSL delay problem on server restarts
	serverSocket_.setReuseAddress(true);
	// Bind to listening port
	serverSocket_.bind(bindAddr);
	} catch (IOException ioe) {
	serverSocket_ = null;
	throw new TTransportException("Could not create ServerSocket on address " + bindAddr.toString() + ".");
	}
}

public void listen() throws TTransportException {
	// Make sure not to block on accept
	if (serverSocket_ != null) {
	try {
		serverSocket_.setSoTimeout(0);
	} catch (SocketException sx) {
		LOGGER.error("Could not set socket timeout.", sx);
	}
	}
}

protected TSocket acceptImpl() throws TTransportException {
	if (serverSocket_ == null) {
	throw new TTransportException(TTransportException.NOT_OPEN, "No underlying server socket.");
	}
	try {
	Socket result = serverSocket_.accept();
	TSocket result2 = createTSocket(result);
	result2.setTimeout(clientTimeout_);
	return result2;
	} catch (IOException iox) {
	throw new TTransportException(iox);
	}
}

protected TSocket createTSocket(Socket socket) throws TTransportException {
	return new TSocket(socket);
}

public void close() {
	if (serverSocket_ != null) {
	try {
		serverSocket_.close();
	} catch (IOException iox) {
		LOGGER.warn("Could not close server socket.", iox);
	}
	serverSocket_ = null;
	}
}

public void interrupt() {
	// The thread-safeness of this is dubious, but Java documentation suggests
	// that it is safe to do this from a different thread context
	close();
}

}
