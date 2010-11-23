package nu.ted.torrent.search;

import java.util.List;

import nu.ted.DataRetrievalException;
import nu.ted.torrent.TorrentRef;

public interface TorrentSourceType {

	String getName();
	String getLocation();

	List<TorrentRef> search(List<String> terms) throws DataRetrievalException;

}
