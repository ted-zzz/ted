package nu.ted.gwt.server.page.watched;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TException;

import nu.ted.client.ClientAction;
import nu.ted.generated.Series;
import nu.ted.generated.TedService.Client;
import nu.ted.gwt.domain.GwtWatchedSeries;

public class GetWatchedSeriesClientAction implements ClientAction {

	private List<GwtWatchedSeries> watched = new ArrayList<GwtWatchedSeries>();

	@Override
	public void run(Client client) throws TException {
		watched.clear();

		List<Series> series = client.getWatching();
		for (Series serie : series) {
			watched.add(new GwtWatchedSeries(serie.getName()));
		}
	}

	public List<GwtWatchedSeries> getWatched() {
		return watched;
	}

}
