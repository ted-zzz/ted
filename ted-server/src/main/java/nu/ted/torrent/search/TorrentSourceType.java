package nu.ted.torrent.search;

import java.util.List;

import nu.ted.generated.Episode;
import nu.ted.torrent.Torrent;

public interface TorrentSourceType {

	String getName();

	List<Torrent> searchEpisode(Episode e);

}
