package nu.ted.gwt.client.page.watched;

import java.util.List;

import nu.ted.gwt.domain.GwtWatchedSeries;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface WatchedSeriesServiceAsync {

	void getWatchedSeries(AsyncCallback<List<GwtWatchedSeries>> callback);

	void stopWatching(short seriesUid, AsyncCallback<Void> messageCallback);

}
