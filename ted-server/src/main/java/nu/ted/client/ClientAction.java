package nu.ted.client;

import org.apache.thrift.TException;

import nu.ted.generated.InvalidOperation;
import nu.ted.generated.TedService.Client;

public interface ClientAction {
	void run(Client client) throws InvalidOperation, TException;
}
