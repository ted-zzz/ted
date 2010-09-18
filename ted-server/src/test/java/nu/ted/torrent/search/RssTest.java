package nu.ted.torrent.search;

import static junit.framework.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import nu.ted.domain.SeriesBackendWrapper;
import nu.ted.generated.Episode;
import nu.ted.generated.Series;
import nu.ted.torrent.TorrentRef;
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

	@Test
	public void ensureSimpleSearchReturnsResult() {
		TestRssSource testRssSource = new TestRssSource();
		List<TorrentRef> torrents = new LinkedList<TorrentRef>();
		torrents.add(new TorrentRef("A-name1", "A-link1"));
		torrents.add(new TorrentRef("A-name2", "A-link2"));
		torrents.add(new TorrentRef("B-name", "B-link"));
		torrents.add(new TorrentRef("C-name", "C-link"));
		testRssSource.addTorrens(torrents);

		Rss rss = new Rss(testRssSource);

		torrents = rss.search(Arrays.asList("A-name"));
		assertNotNull(torrents);
		assertEquals(2, torrents.size());

		assertEquals("A-name1" , torrents.get(0).getTitle());
		assertEquals("A-name2" , torrents.get(1).getTitle());
	}

	@Test
	public void ensureSearchEpisodeWorks() {
		TestRssSource testRssSource = new TestRssSource();
		List<TorrentRef> torrents = new LinkedList<TorrentRef>();
		torrents.add(new TorrentRef("Show That Matches S01E01", "link1"));
		torrents.add(new TorrentRef("Show That Matches S01E02", "link2"));
		torrents.add(new TorrentRef("Show That Matches S01E02", "link3"));
		torrents.add(new TorrentRef("Show No Match S01E01", "link4"));
		torrents.add(new TorrentRef("Show No Match S01E02", "link5"));
		testRssSource.addTorrens(torrents);

		Rss rss = new Rss(testRssSource);

		Series series = new Series();
		series.setName("Show That Matches");

		Episode episode = new Episode();
		episode.setSeason((short) 1);
		episode.setNumber((short) 2);

		torrents = rss.searchEpisode(new SeriesBackendWrapper(series), episode);

		assertEquals(2, torrents.size());

		TorrentRef one = torrents.get(0);
		TorrentRef two = torrents.get(1);
		if (one.getLink().equals("link2")) {
			if (!two.getLink().equals("link3")) {
				fail("Didn't return correct results");
			}
		} else if (one.getLink().equals("link3")) {
			if (!two.getLink().equals("link2")) {
				fail("Didn't return correct results");
			}
		} else {
			fail("First torrent didn't have either link correct");
		}

	}

	// Set location and a breakpoint to test Rome's processing.
	// Could have RomeRssSouce() take an input stream for testing.
	public void manualRomeDebug() {
		String location = "set location here";
		RomeRssSource source = new RomeRssSource(location);
		source.getTorrents();
	}

}