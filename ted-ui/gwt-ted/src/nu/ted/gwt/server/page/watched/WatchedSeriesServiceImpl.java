package nu.ted.gwt.server.page.watched;

import java.util.List;

import nu.ted.client.JavaClient;
import nu.ted.gwt.client.page.watched.WatchedSeriesService;
import nu.ted.gwt.domain.GwtWatchedSeries;
import nu.ted.gwt.server.page.search.TedRemoteServiceServlet;

public class WatchedSeriesServiceImpl extends TedRemoteServiceServlet
	implements WatchedSeriesService {

	@Override
	public List<GwtWatchedSeries> getWatchedSeries() {
		JavaClient tedClient = getTedClient();
		GetWatchedSeriesClientAction action = new GetWatchedSeriesClientAction(getImageStore());
		tedClient.run(action);
		return action.getWatched();
	}


}
