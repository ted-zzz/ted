package nu.ted;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import nu.ted.domain.EpisodeBackendWrapper;
import nu.ted.generated.TDate;
import nu.ted.generated.Episode;
import nu.ted.generated.EpisodeStatus;
import nu.ted.generated.Series;
import nu.ted.generated.Ted;
import nu.ted.generated.TedConfig;
import nu.ted.generated.TorrentSource;
import nu.ted.torrent.TorrentRef;
import nu.ted.torrent.TorrentTitleMatcher;
import nu.ted.torrent.search.TorrentSourceIndex;
import nu.ted.torrent.search.TorrentSourceType;
import nu.ted.torrent.search.TorrentSourceTypeFactory;

import org.junit.Test;

public class SearcherTest {

	public static class TestTorrentSourceFactory implements TorrentSourceTypeFactory {

		// Always use the same one here for testing.
		TestTorrentSource torrentSource;

		public TestTorrentSourceFactory(TestTorrentSource testTorrentSource) {
			torrentSource = testTorrentSource;
		}

		@Override
		public TorrentSourceType createTorrentSourceType(TorrentSource source) {
			return torrentSource;
		}

	}
	public static class TestTorrentSourceWithException extends TestTorrentSource {

		@Override
		public String getName() {
			return "testTorrentSourceTypeWithException";
		}

		@Override
		public String getLocation() {
			throw new UnsupportedOperationException("Not Yet Implemented");
		}

		public List<TorrentRef> search(List<TorrentTitleMatcher> terms)
				throws DataRetrievalException {
			throw new DataRetrievalException("expected");
		}

	}
	public static class TestTorrentSource implements TorrentSourceType {

		int searchesDone = 0;

		@Override
		public String getName() {
			return "testTorrentSourceType";
		}

		@Override
		public List<TorrentRef> search(List<TorrentTitleMatcher> terms) throws DataRetrievalException {
			searchesDone++;
			return new LinkedList<TorrentRef>();
		}

		@Override
		public String getLocation() {
			throw new UnsupportedOperationException("Not Yet Implemented");
		}
	}

	@Test
	public void ensureSearchesForMissingEpisodesCleanupTorrentSourceIndex() throws Exception {
		try {
			ensureHasMissingFindsMissingsEpsidoes();
		} finally {
			TorrentSourceIndex.clear();
		}
	}

	/* Not a huge fan of how this currently works. Because the factories are global
	 * the test needs to be wrapped to make sure it is properly cleaned up.
	 */
	public void ensureHasMissingFindsMissingsEpsidoes() throws Exception {
		TestTorrentSource testTorrentSource = new TestTorrentSource();
		TestTorrentSource testTorrentSourceWithE = new TestTorrentSourceWithException();

		TorrentSourceIndex.registerFactory(testTorrentSource.getName(), new TestTorrentSourceFactory(
				testTorrentSource));
		TorrentSourceIndex.registerFactory(testTorrentSourceWithE.getName(), new TestTorrentSourceFactory(
				testTorrentSourceWithE));

		TorrentSource torrentSource = new TorrentSource((short) 1, testTorrentSource.getName(), "freetv", "freetv.invalid");
		TorrentSource torrentSourceWithE = new TorrentSource((short) 2, testTorrentSourceWithE.getName(), "BAD", "BAD.invalid");
		List<TorrentSource> sources = new LinkedList<TorrentSource>();

		sources.add(torrentSourceWithE);
		sources.add(torrentSource);

		Episode s1e2 = new Episode((short) 1, (short) 2, new TDate(12345));
		s1e2.setStatus(EpisodeStatus.SEARCHING);

		Episode s1e3 = new Episode((short) 1, (short) 3, new TDate(12345));
		s1e3.setStatus(EpisodeStatus.SEARCHING);

		Episode s1e4 = new Episode((short) 1, (short) 4, new TDate(12345));
		s1e4.setStatus(EpisodeStatus.FOUND);

		Episode s2e1 = new Episode((short) 1, (short) 5, new TDate(12345));
		s2e1.setStatus(EpisodeStatus.FOUND);

		List<Episode> s1Eplist = new LinkedList<Episode>();
		s1Eplist.add(s1e2);
		s1Eplist.add(s1e3);
		s1Eplist.add(s1e4);
		Series s1 = new Series((short) 1, "name", new TDate(12344), "guideName", "guideId", s1Eplist);

		List<Episode> s2EpList = new LinkedList<Episode>();
		s2EpList.add(s2e1);
		Series s2 = new Series((short) 2, "name", new TDate(12346), "guideName", "guildId", s2EpList);

		List<Series> series = new LinkedList<Series>();
		series.add(s1);
		series.add(s2);

		Ted ted = new Ted();
		ted.setConfig(new TedConfig());
		ted.getConfig().setTorrentSources(sources);
		ted.setSeries(series);

		Searcher search = new Searcher();
		Searcher.setup(null, ted);

		search.searchForMissingEpisodes();

		assertEquals(2, testTorrentSource.searchesDone);
	}
}
