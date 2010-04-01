package nu.ted.gwt.server.page.watched;

import org.apache.thrift.TException;

import nu.ted.client.ClientAction;
import nu.ted.generated.TedService.Client;

public class StopWatchingSeriesClientAction implements ClientAction {

	private final short seriesUid;

	public StopWatchingSeriesClientAction(short seriesUid) {
		this.seriesUid = seriesUid;
	}

	@Override
	public void run(Client client) throws TException {
		client.stopWatching(this.seriesUid);
	}

}
