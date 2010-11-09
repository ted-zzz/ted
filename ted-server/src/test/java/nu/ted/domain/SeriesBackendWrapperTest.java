package nu.ted.domain;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import nu.ted.Server;
import nu.ted.SearcherTest.TestTorrentSource;
import nu.ted.generated.Date;
import nu.ted.generated.Episode;
import nu.ted.generated.EpisodeStatus;
import nu.ted.generated.Event;
import nu.ted.generated.EventType;
import nu.ted.generated.Series;
import nu.ted.guide.GuideFactory;
import nu.ted.guide.TestGuide;
import nu.ted.service.TedServiceImpl;

import static org.junit.Assert.*;

import org.junit.Test;

public class SeriesBackendWrapperTest {

	@Test
	public void ensureEventFiredWhenEpisodeAdded() throws Exception {
		Date lastPoll = new Date(Calendar.getInstance().getTimeInMillis());
		TestGuide seriesSource = new TestGuide();
		GuideFactory.addGuide(seriesSource);
		TedServiceImpl service = new TedServiceImpl(Server.createDefaultTed(), seriesSource);

		Series series = new Series();
		series.setGuideName(seriesSource.getName());
		SeriesBackendWrapper wrapper = new SeriesBackendWrapper(series);
		wrapper.update(Calendar.getInstance());
		List<Event> events = service.getEvents(lastPoll);
		assertEquals(1, events.size());

		Event received = events.get(0);
		assertEquals(EventType.EPISODE_ADDED, received.getType());
		assertEquals(series, received.getSeries());
	}

	@Test
	public void ensureSearchTermsAreReturnedCorrectly() {
		Series series = new Series((short) 0, "name with lots of spaces", null, null, null, null);
		SeriesBackendWrapper wrapper = new SeriesBackendWrapper(series);

		assertArrayEquals(new String[] {"name", "with", "lots", "of", "spaces"}, wrapper.getSearchTerms().toArray(new String[] {}));
	}

	@Test
	public void ensureSeriesKnowsItsMissingEpisodes() throws Exception {
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

		List<Episode> s2EpList = new LinkedList<Episode>();
		s2EpList.add(s2e1);
		Series s2 = new Series((short) 2, "name", new Date(12346), "guideName", "guildId", s2EpList);

		assertTrue(new SeriesBackendWrapper(s1).hasMissingEpisodes());
		assertFalse(new SeriesBackendWrapper(s2).hasMissingEpisodes());
	}
}
