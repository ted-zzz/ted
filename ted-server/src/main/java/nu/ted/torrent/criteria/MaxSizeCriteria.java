package nu.ted.torrent.criteria;

import nu.ted.torrent.TorrentRef;

public class MaxSizeCriteria implements Criteria {

	private long size;

	public MaxSizeCriteria(long size) {
		this.size = size;
	}

	@Override
	public boolean isAcceptable(TorrentRef torrent) {

		return torrent.getSize() <= size;
	}

}
