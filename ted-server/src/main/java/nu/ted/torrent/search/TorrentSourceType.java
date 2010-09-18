package nu.ted.torrent.search;

import java.util.List;

import nu.ted.domain.SeriesBackendWrapper;
import nu.ted.generated.Episode;
import nu.ted.torrent.TorrentRef;

public interface TorrentSourceType {

	String getName();
	String getLocation();

	List<TorrentRef> searchEpisode(SeriesBackendWrapper series, Episode episode);

}
