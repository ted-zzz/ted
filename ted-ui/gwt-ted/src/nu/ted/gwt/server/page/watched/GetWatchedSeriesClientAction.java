package nu.ted.gwt.server.page.watched;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TException;

import nu.ted.client.ClientAction;
import nu.ted.generated.WatchedSeries;
import nu.ted.generated.TedService.Client;
import nu.ted.gwt.domain.GwtWatchedSeries;

public class GetWatchedSeriesClientAction implements ClientAction {

	private List<GwtWatchedSeries> watched = new ArrayList<GwtWatchedSeries>();

	@Override
	public void run(Client client) throws TException {
		watched.clear();

		List<WatchedSeries> series = client.getWatching();
		for (WatchedSeries serie : series) {
			watched.add(new GwtWatchedSeries(serie.getName()));
		}
	}

	public List<GwtWatchedSeries> getWatched() {
		return watched;
	}

}
