package nu.ted.torrent.criteria;

import nu.ted.torrent.TorrentRef;

public interface Criteria {

	boolean isAcceptable(TorrentRef torrent);

}
