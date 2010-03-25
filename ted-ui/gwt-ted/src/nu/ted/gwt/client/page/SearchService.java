package nu.ted.gwt.client.page;

import java.util.List;

import nu.ted.gwt.domain.SearchShowInfo;
import nu.ted.gwt.domain.ShowSearchResult;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("search")
public interface SearchService extends RemoteService {
    public List<ShowSearchResult> search(String filter);
    public SearchShowInfo getShowInfo(String searchUUID);
}
