package nu.ted.torrent.search;

import java.util.LinkedList;
import java.util.List;

import nu.ted.torrent.Torrent;

public class Rss {
	
	public static interface RssSource {
		public List<Torrent> getTorrents();
	}
	
	public static class RomeRssSource implements RssSource {

		@Override
		public List<Torrent> getTorrents() {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Not Yet Implemented");
		}
	}
	
	private RssSource source;
	
	public Rss(RssSource source) {
		this.source = source;
	}
	
	public Rss() {
		this.source = new RomeRssSource();
	}

	public List<Torrent> search(String term) {
		List<Torrent> torrents = source.getTorrents();
		List<Torrent> results = new LinkedList<Torrent>();
		
		for (Torrent torrent : torrents) {
			if (torrent.getTitle().contains(term)) {
				results.add(torrent);
			}
		}
		
		return results;
	}

}
