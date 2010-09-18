package nu.ted.torrent.search;

import nu.ted.generated.TorrentSource;

public interface TorrentSourceTypeFactory {

	public TorrentSourceType createTorrentSourceType(TorrentSource source);

}
