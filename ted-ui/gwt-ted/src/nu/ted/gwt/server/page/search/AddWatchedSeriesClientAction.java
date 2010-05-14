package nu.ted.gwt.server.page.search;

import org.apache.thrift.TException;

import nu.ted.client.ClientAction;
import nu.ted.generated.InvalidOperation;
import nu.ted.generated.TedService.Client;

public class AddWatchedSeriesClientAction implements ClientAction {

	private String searchId;

	public AddWatchedSeriesClientAction(String searchID) {
		this.searchId = searchID;
	}

	// TODO: Not sure how you'll want to handle this on the GWT side.
	@Override
	public void run(Client client) throws TException, InvalidOperation {
		client.startWatching(searchId);
	}

}
