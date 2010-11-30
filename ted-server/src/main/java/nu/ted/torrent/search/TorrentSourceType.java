package nu.ted.torrent.search;

import java.util.List;

import nu.ted.DataRetrievalException;
import nu.ted.torrent.TorrentRef;
import nu.ted.torrent.TorrentTitleMatcher;

public interface TorrentSourceType {

	String getName();
	String getLocation();

	List<TorrentRef> search(List<TorrentTitleMatcher> matchers) throws DataRetrievalException;

}
