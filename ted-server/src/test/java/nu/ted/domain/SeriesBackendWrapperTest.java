package nu.ted.domain;

import java.util.Calendar;
import java.util.List;

import nu.ted.Server;
import nu.ted.generated.Event;
import nu.ted.generated.EventType;
import nu.ted.generated.Series;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.guide.GuideFactory;
import nu.ted.guide.TestGuide;
import nu.ted.guide.tvdb.TVDB;
import nu.ted.service.TedServiceImpl;

import static org.junit.Assert.*;
import org.junit.Test;

public class SeriesBackendWrapperTest {

	@Test
	public void ensureEventFiredWhenEpisodeAdded() throws Exception {
		TestGuide seriesSource = new TestGuide();
		GuideFactory.addGuide(seriesSource);
		TedServiceImpl service = new TedServiceImpl(Server.createDefaultTed(), seriesSource);
		String clientId = service.registerClientWithEventRegistry();

		Series series = new Series();
		series.setGuideName(seriesSource.getName());
		SeriesBackendWrapper wrapper = new SeriesBackendWrapper(series);
		wrapper.update(Calendar.getInstance());
		List<Event> events = service.getEvents(clientId);
		assertEquals(1, events.size());

		Event received = events.get(0);
		assertEquals(EventType.EPISODE_ADDED, received.getType());
		assertEquals(series, received.getSeries());
	}

}
