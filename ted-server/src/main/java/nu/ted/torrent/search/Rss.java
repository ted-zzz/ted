package nu.ted.torrent.search;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

import nu.ted.domain.SeriesBackendWrapper;
import nu.ted.generated.Episode;
import nu.ted.guide.DataTransferException;
import nu.ted.torrent.Torrent;
import nu.ted.www.DirectPageLoader;
import nu.ted.www.PageLoader;

public class Rss implements TorrentSourceType {

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

	private boolean titleContainsTerms(Torrent torrent, List<String> terms) {
		for (String term : terms) {
			if (!torrent.getTitle().contains(term)) {
				return false;
			}
		}
		return true;
	}

	public List<Torrent> search(List<String> terms) {
		List<Torrent> torrents = source.getTorrents();
		List<Torrent> results = new LinkedList<Torrent>();

		for (Torrent torrent : torrents) {
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
	public List<Torrent> searchEpisode(SeriesBackendWrapper series, Episode episode) {

		List<String> terms = new LinkedList<String>(Arrays.asList(series.getSearchTerms()));

		NumberFormat formatter = new DecimalFormat("00");

		// TODO: abstract this out somewhere later
		String searchNumber = "S" + formatter.format(episode.getSeason()) +
				"E" + formatter.format(episode.getNumber());

		terms.add(searchNumber);

		return search(terms);
	}

}