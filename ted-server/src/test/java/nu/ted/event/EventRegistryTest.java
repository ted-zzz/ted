package nu.ted.event;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nu.ted.generated.Event;
import nu.ted.generated.EventType;

import org.junit.Test;

public class EventRegistryTest {

	@Test
	public void ensureGetEventsReturnsOnlyEventsSinceLastCheckDate() {
		TestEventRegistry registry = new TestEventRegistry();
		registry.setNextRegisterDate(getTime(Calendar.MAY, 11, 2011));
		registry.addEvent(EventFactory.createEpisodeAddedEvent(null, null));
		registry.setNextRegisterDate(getTime(Calendar.JUNE, 3, 2009));
		registry.addEvent(EventFactory.createWatchedSeriesRemovedEvent(null));

		List<Event> events = registry.getEvents(getTime(Calendar.NOVEMBER, 2, 2010));
		assertEquals(1, events.size());
		assertEquals(EventType.EPISODE_ADDED, events.get(0).getType());
	}

	@Test
	public void ensureGetEventsDoesNotIncludeEventsPostedOnExactLastPostTime() {
		TestEventRegistry registry = new TestEventRegistry();
		Date lastPoll = getTime(Calendar.MAY, 11, 2011);
		registry.setNextRegisterDate(lastPoll);
		registry.addEvent(EventFactory.createEpisodeAddedEvent(null, null));

		List<Event> events = registry.getEvents(lastPoll);
		assertEquals(0, events.size());
	}

	@Test
	public void ensureCleanUpCleansRegistryAccordingToMaxAge() {
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();


		TestEventRegistry registry = new TestEventRegistry();

		cal.add(Calendar.MINUTE, -2);
		registry.setNextRegisterDate(cal.getTime());
		registry.addEvent(EventFactory.createEpisodeAddedEvent(null, null));

		cal.add(Calendar.MINUTE, -10);
		registry.setNextRegisterDate(cal.getTime());
		registry.addEvent(EventFactory.createWatchedSeriesRemovedEvent(null));

		cal.add(Calendar.MINUTE, -9);
		registry.setNextRegisterDate(cal.getTime());
		registry.addEvent(EventFactory.createWatchedSeriesRemovedEvent(null));

		cal.add(Calendar.MINUTE, -60);
		Date lastPoll = cal.getTime();

		assertEquals(3, registry.getEvents(lastPoll).size());

		registry.cleanup(30); // Nothing is that old.
		assertEquals(3, registry.getEvents(lastPoll).size());

		registry.cleanup(15);
		assertEquals(2, registry.getEvents(lastPoll).size());

		registry.cleanup(1);
		assertTrue(registry.getEvents(lastPoll).isEmpty());

	}

	private Date getTime(int calMonth, int day, int year, int min) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, calMonth);
		cal.set(Calendar.DATE, day);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	private Date getTime(int calMonth, int day, int year) {
		return getTime(calMonth, day, year, 0);
	}

}
