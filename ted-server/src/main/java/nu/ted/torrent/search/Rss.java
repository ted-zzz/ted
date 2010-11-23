package nu.ted.torrent.search;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.sun.syndication.feed.rss.Enclosure;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

import nu.ted.DataRetrievalException;
import nu.ted.generated.TorrentSource;
import nu.ted.torrent.TorrentRef;
import nu.ted.www.DirectPageLoader;
import nu.ted.www.PageLoader;

public class Rss implements TorrentSourceType {

	public static String name = "RSS";

	public static class RssFactory implements TorrentSourceTypeFactory {

		protected RssSource getRssSource(TorrentSource source) {
			return new RomeRssSource(source.getLocation());
		}

		@Override
		public TorrentSourceType createTorrentSourceType(TorrentSource torrentSource) {
			return new Rss(getRssSource(torrentSource));
		}

	}

	public static interface RssSource {
		public List<TorrentRef> getTorrents() throws DataRetrievalException;
		public String getLocation();
	}

	public static class RomeRssSource implements RssSource {

		private String location;

		public RomeRssSource(String location) {
			this.location = location;
		}


		@Override
		public List<TorrentRef> getTorrents() throws DataRetrievalException {
			List<TorrentRef> results = new LinkedList<TorrentRef>();

			try {
				PageLoader pageLoader = new DirectPageLoader();
				InputStream inputStream = pageLoader.openStream(location);
				SyndFeedInput feedInput = new SyndFeedInput();
				SyndFeed feed = feedInput.build(new InputStreamReader(inputStream));

				@SuppressWarnings("rawtypes")
				Iterator iter = feed.getEntries().iterator();
				while (iter.hasNext()) {
					SyndEntry entry = (SyndEntry) iter.next();
					for (Enclosure enclosure : getEnclosures(entry)) {
						if (enclosure.getType() == "application/x-bittorrent") {
							results.add(new TorrentRef(entry.getTitle(), entry.getLink(), enclosure.getLength()));
							break;
						}
					}
				}
			} catch (IllegalArgumentException e) {
				throw new DataRetrievalException(e);
			} catch (FeedException e) {
				throw new DataRetrievalException(e);
			}

			return results;
		}


		@SuppressWarnings("unchecked")
		private List<Enclosure> getEnclosures(SyndEntry entry) {
			return (List<Enclosure>)entry.getEnclosures();
		}

		@Override
		public String getLocation() {
			return location;
		}
	}

	private RssSource source;

	public Rss(RssSource source) {
		this.source = source;
	}

	private boolean titleContainsTerms(TorrentRef torrent, List<String> terms) {
		for (String term : terms) {
			if (!torrent.getTitle().contains(term)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public List<TorrentRef> search(List<String> terms) throws DataRetrievalException {
		List<TorrentRef> torrents = source.getTorrents();
		List<TorrentRef> results = new LinkedList<TorrentRef>();

		for (TorrentRef torrent : torrents) {
			if (titleContainsTerms(torrent, terms)) {
				results.add(torrent);
			}
		}

		return results;
	}

	@Override
	public String getName() {
		return "RSS";
	}

	@Override
	public String getLocation() {
		return source.getLocation();
	}


}
