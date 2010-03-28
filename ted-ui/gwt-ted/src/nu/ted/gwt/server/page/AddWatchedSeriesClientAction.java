package nu.ted.gwt.server.page;

import org.apache.thrift.TException;

import nu.ted.client.ClientAction;
import nu.ted.generated.TedService.Client;

public class AddWatchedSeriesClientAction implements ClientAction {

	private String searchId;

	public AddWatchedSeriesClientAction(String searchID) {
		this.searchId = searchID;
	}

	@Override
	public void run(Client client) throws TException {
		client.startWatching(searchId);
	}

}
