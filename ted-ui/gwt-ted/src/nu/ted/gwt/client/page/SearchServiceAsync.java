package nu.ted.gwt.client.page;

import java.util.List;

import nu.ted.gwt.domain.FoundSeries;
import nu.ted.gwt.domain.SearchSeriesInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SearchServiceAsync {

	void search(String filter, AsyncCallback<List<FoundSeries>> callback);

	void getSeriesInfo(String searchUUID, AsyncCallback<SearchSeriesInfo> callback);

	void addWatchedSeries(String searchId, AsyncCallback<Void> messageCallback);

}
