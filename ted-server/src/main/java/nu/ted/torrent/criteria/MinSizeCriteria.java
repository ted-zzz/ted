package nu.ted.torrent.criteria;

import nu.ted.torrent.TorrentRef;

public class MinSizeCriteria implements Criteria {

	private long size;

	public MinSizeCriteria(long size) {
		this.size = size;
	}

	@Override
	public boolean isAcceptable(TorrentRef torrent) {

		return torrent.getSize() >= size;
	}

}
