package nu.ted.gwt.server.page.watched;

import java.util.ArrayList;
import java.util.List;

import nu.ted.gwt.client.page.watched.WatchedSeriesService;
import nu.ted.gwt.domain.GwtWatchedSeries;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class WatchedSeriesServiceImpl extends RemoteServiceServlet
	implements WatchedSeriesService {

	@Override
	public List<GwtWatchedSeries> getWatchedSeries() {

		ArrayList<GwtWatchedSeries> watchedList = new ArrayList<GwtWatchedSeries>();
		watchedList.add(new GwtWatchedSeries("Big Bang Theory"));
		watchedList.add(new GwtWatchedSeries("Super Bikes"));
		watchedList.add(new GwtWatchedSeries("Modern Family"));
		return watchedList;
	}


}
