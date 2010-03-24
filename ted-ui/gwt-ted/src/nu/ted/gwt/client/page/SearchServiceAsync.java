package nu.ted.gwt.client.page;

import java.util.List;

import nu.ted.gwt.domain.SearchShowInfo;
import nu.ted.gwt.domain.ShowSearchResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SearchServiceAsync {

    void search(String filter, AsyncCallback<List<ShowSearchResult>> callback);

    void loadBanner(String searchUUID, AsyncCallback<SearchShowInfo> callback);

}
