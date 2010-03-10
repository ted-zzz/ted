package nu.ted.client;

import org.apache.thrift.TException;

import nu.ted.gen.TedService.Client;

public interface ClientAction {
	void run(Client client) throws TException;
}
