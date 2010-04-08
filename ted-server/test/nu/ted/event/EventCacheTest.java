package nu.ted.event;

import static org.junit.Assert.*;

import java.util.List;

import nu.ted.generated.Event;
import nu.ted.generated.EventType;

import org.junit.Test;

public class EventCacheTest {

	@Test
	public void ensureLastPollTimeIsSetOnCreation() {
		EventCache cache = new EventCache();
		assertFalse("Last Poll Time still has default.", 0L == cache.getLastPollTime());
	}

	@Test
	public void ensureLastPollTimeIsUpdatedOnGetEvents() {
		EventCache cache = new EventCache();
		long lastPollTime = cache.getLastPollTime();
		cache.getEvents();
		assertFalse("Last poll time was not updated.", lastPollTime < cache.getLastPollTime());
	}

	@Test
	public void ensureGetEventsReturnsDetachedListOfEvents() {
		EventCache cache = new EventCache();
		cache.addEvent(new Event(EventType.WATCHED_SERIES_ADDED, null));
		List<Event> fetched = cache.getEvents();
		assertEquals(1, fetched.size());
		cache.clear();

		List<Event> clearedList = cache.getEvents();
		assertEquals(0, clearedList.size());
		assertEquals(1, fetched.size());
	}

}
