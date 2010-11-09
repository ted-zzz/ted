package nu.ted.event;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nu.ted.generated.Event;
import nu.ted.generated.EventType;

import org.junit.Test;

public class EventRegistryCleanerTest {

	@Test
	public void ensureCacheCleanerCleansExpiredEvents() throws Exception {
		TestEventRegistry registry = new TestEventRegistry();

		// Should not get cleaned.
		registry.setNextRegisterDate(new Date());
		registry.addEvent(EventFactory.createWatchedSeriesAddedEvent(null));

		// Should get cleaned - 30 min old.
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -30);
		registry.setNextRegisterDate(cal.getTime());
		registry.addEvent(EventFactory.createWatchedSeriesRemovedEvent(null));

		EventRegistryCleaner cleaner = new EventRegistryCleaner(registry, 0, 20);
		cleaner.start();

		// Don't like having sleeps in tests (yuck), but until
		// I find time to look into it further, a 1 millisecond
		// delay is not too bad.
		Thread.sleep(1);
		cleaner.kill();

		cal.set(Calendar.MINUTE, -10);
		List<Event> fetched = registry.getEvents(cal.getTime());
		assertEquals(fetched.size(), 1);
		assertEquals(fetched.get(0).getType(), EventType.WATCHED_SERIES_ADDED);
	}

}
