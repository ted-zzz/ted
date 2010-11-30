package nu.ted.torrent.search;

import static junit.framework.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import nu.ted.DataRetrievalException;
import nu.ted.torrent.TorrentRef;
import nu.ted.torrent.TorrentTitleMatcher;
import nu.ted.torrent.search.Rss.RomeRssSource;
import nu.ted.torrent.search.Rss.RssSource;

import org.junit.Test;

public class RssTest {

	private static class TestRssSource implements RssSource {

		private List<TorrentRef> torrents;

		@Override
		public List<TorrentRef> getTorrents() {

			return torrents;
		}

		@Override
		public String getLocation() {
			return "test-location";
		}

		public void addTorrens(List<TorrentRef> torrents) {
			this.torrents = torrents;
		}
	}

	private static class SimpleStringMatcher implements TorrentTitleMatcher {

		String term;

		SimpleStringMatcher(String term) {
			this.term = term;
		}

		@Override
		public boolean matchTitle(String title) {
			return title.contains(term);
		}

	}

	@Test
	public void ensureSimpleSearchReturnsResult() throws DataRetrievalException {
		TestRssSource testRssSource = new TestRssSource();
		List<TorrentRef> torrents = new LinkedList<TorrentRef>();
		torrents.add(new TorrentRef("A-name1", "A-link1", 100));
		torrents.add(new TorrentRef("A-name2", "A-link2", 100));
		torrents.add(new TorrentRef("B-name", "B-link", 100));
		torrents.add(new TorrentRef("C-name", "C-link", 100));
		testRssSource.addTorrens(torrents);

		Rss rss = new Rss(testRssSource);

		List<TorrentTitleMatcher> testMatcher = new LinkedList<TorrentTitleMatcher>();
		testMatcher.add(new SimpleStringMatcher("A-name"));
		torrents = rss.search(testMatcher);
		assertNotNull(torrents);
		assertEquals(2, torrents.size());

		assertEquals("A-name1" , torrents.get(0).getTitle());
		assertEquals("A-name2" , torrents.get(1).getTitle());
	}

	// Set location and a breakpoint to test Rome's processing.
	// Could have RomeRssSouce() take an input stream for testing.
	public void manualRomeDebug() throws DataRetrievalException {
		String location = "set location here";
		RomeRssSource source = new RomeRssSource(location);
		source.getTorrents();
	}

}