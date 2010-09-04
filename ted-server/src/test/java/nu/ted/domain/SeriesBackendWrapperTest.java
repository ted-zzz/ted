package nu.ted.domain;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import nu.ted.Server;
import nu.ted.generated.Date;
import nu.ted.generated.Episode;
import nu.ted.generated.EpisodeStatus;
import nu.ted.generated.Event;
import nu.ted.generated.EventType;
import nu.ted.generated.Series;
import nu.ted.generated.TorrentSource;
import nu.ted.generated.TorrentSourceUnion;
import nu.ted.guide.GuideFactory;
import nu.ted.guide.TestGuide;
import nu.ted.service.TedServiceImpl;
import nu.ted.torrent.Torrent;
import nu.ted.torrent.search.TorrentSourceTypeIndex;
import nu.ted.torrent.search.TorrentSourceType;

import static org.junit.Assert.*;
import org.junit.Test;

public class SeriesBackendWrapperTest {

	@Test
	public void ensureEventFiredWhenEpisodeAdded() throws Exception {
		TestGuide seriesSource = new TestGuide();
		GuideFactory.addGuide(seriesSource);
		TedServiceImpl service = new TedServiceImpl(Server.createDefaultTed(), seriesSource);
		service.registerClientWithEventRegistry();

		Series series = new Series();
		series.setGuideName(seriesSource.getName());
		SeriesBackendWrapper wrapper = new SeriesBackendWrapper(series);
		wrapper.update(Calendar.getInstance());
		List<Event> events = service.getEvents();
		assertEquals(1, events.size());

		Event received = events.get(0);
		assertEquals(EventType.EPISODE_ADDED, received.getType());
		assertEquals(series, received.getSeries());
	}

	public static class TestTorrentSource implements TorrentSourceType {

		List<Episode> searchedEpisodes = new LinkedList<Episode>();

		@Override
		public String getName() {
			return "testTorrentSourceType";
		}

		@Override
		public List<Torrent> searchEpisode(SeriesBackendWrapper series, Episode e) {
			searchedEpisodes.add(e);
			return new LinkedList<Torrent>();
		}
	}

	@Test
	public void ensureHasMissingFindsMissingsEpsidoes() throws Exception {
		TestTorrentSource testTorrentSource = new TestTorrentSource();

		TorrentSourceTypeIndex.addType(testTorrentSource);

		TorrentSource torrentSource = new TorrentSource(testTorrentSource.getName(), "freetv", "freetv.invalid");
		List<TorrentSource> sources = new LinkedList<TorrentSource>();
		sources.add(torrentSource);
		TorrentSourceUnion torrentSourceUnion = new TorrentSourceUnion();
		torrentSourceUnion.setSources(sources);

		Episode s1e1 = new Episode((short) 1, (short) 2, new Date(12345));
		s1e1.setStatus(EpisodeStatus.SEARCHING);

		Episode s1e2 = new Episode((short) 1, (short) 3, new Date(12345));
		s1e2.setStatus(EpisodeStatus.SEARCHING);

		Episode s1e3 = new Episode((short) 1, (short) 4, new Date(12345));
		s1e3.setStatus(EpisodeStatus.FOUND);

		Episode s2e1 = new Episode((short) 1, (short) 5, new Date(12345));
		s2e1.setStatus(EpisodeStatus.FOUND);

		List<Episode> s1Eplist = new LinkedList<Episode>();
		s1Eplist.add(s1e1);
		s1Eplist.add(s1e2);
		s1Eplist.add(s1e3);
		Series s1 = new Series((short) 1, "name", new Date(12344), "guideName", "guideId", s1Eplist);
		s1.setSources(torrentSourceUnion);

		List<Episode> s2EpList = new LinkedList<Episode>();
		s2EpList.add(s2e1);
		Series s2 = new Series((short) 2, "name", new Date(12346), "guideName", "guildId", s2EpList);
		s2.setSources(torrentSourceUnion);

		assertTrue(new SeriesBackendWrapper(s1).hasMissingEpisodes());
		assertFalse(new SeriesBackendWrapper(s2).hasMissingEpisodes());

		new SeriesBackendWrapper(s1).searchForMissingEpisodes();
		assertEquals(2, testTorrentSource.searchedEpisodes.size());
		assertTrue(testTorrentSource.searchedEpisodes.contains(s1e1));
		assertTrue(testTorrentSource.searchedEpisodes.contains(s1e2));
		assertFalse(testTorrentSource.searchedEpisodes.contains(s1e3));

		testTorrentSource.searchedEpisodes.clear();

		new SeriesBackendWrapper(s2).searchForMissingEpisodes();
		assertTrue(testTorrentSource.searchedEpisodes.isEmpty());
	}

	@Test
	public void ensureSearchTermsAreReturnedCorrectly() {
		Series series = new Series((short) 0, "name with lots of spaces", null, null, null, null);
		SeriesBackendWrapper wrapper = new SeriesBackendWrapper(series);

		assertArrayEquals(new String[] {"name", "with", "lots", "of", "spaces"}, wrapper.getSearchTerms());
	}

}
