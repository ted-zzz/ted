package nu.ted.torrent.search;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

import nu.ted.guide.DataTransferException;
import nu.ted.torrent.Torrent;
import nu.ted.www.DirectPageLoader;
import nu.ted.www.PageLoader;

public class Rss {

	public static interface RssSource {
		public List<Torrent> getTorrents();
	}

	public static class RomeRssSource implements RssSource {

		private String location;

		public RomeRssSource(String location) {
			this.location = location;
		}

		@Override
		public List<Torrent> getTorrents() {
			List<Torrent> results = new LinkedList<Torrent>();

			try {
				PageLoader pageLoader = new DirectPageLoader();
				InputStream inputStream = pageLoader.openStream(location);
				SyndFeedInput feedInput = new SyndFeedInput();
				SyndFeed feed = feedInput.build(new InputStreamReader(inputStream));

				@SuppressWarnings("rawtypes")
				Iterator iter = feed.getEntries().iterator();
				while (iter.hasNext()) {
					SyndEntry entry = (SyndEntry) iter.next();
					results.add(new Torrent(entry.getTitle(), entry.getLink()));
				}
			} catch (DataTransferException e) {
				// TODO: log error - probably ignore
				throw new RuntimeException("Unhandled DTE", e);
			} catch (IllegalArgumentException e) {
				// TODO: log error - probably ignore
				throw new RuntimeException("Unhandled IAE", e);
			} catch (FeedException e) {
				// TODO: log error - probably ignore
				throw new RuntimeException("Unhandled FE", e);
			}

			return results;
		}
	}

	private RssSource source;

	public Rss(RssSource source) {
		this.source = source;
	}

	public Rss(String location) {
		this.source = new RomeRssSource(location);
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
