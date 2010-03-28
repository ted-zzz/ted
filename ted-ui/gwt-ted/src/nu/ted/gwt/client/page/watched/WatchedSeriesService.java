package nu.ted.gwt.client.page.watched;

import java.util.List;

import nu.ted.gwt.domain.GwtWatchedSeries;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("watched")
public interface WatchedSeriesService extends RemoteService {

	List<GwtWatchedSeries> getWatchedSeries();

}
