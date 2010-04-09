package nu.ted.gwt.server.event;

import org.apache.thrift.TException;

import nu.ted.client.ClientAction;
import nu.ted.generated.TedService.Client;

public class RegisterClientAcion implements ClientAction {

	private String clientId;

	@Override
	public void run(Client client) throws TException {
		clientId = client.registerClientWithEventRegistry();
	}

	public String getClientId() {
		return this.clientId;
	}

}
