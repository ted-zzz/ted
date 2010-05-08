package nu.ted.event;

import nu.ted.generated.Event;
import nu.ted.generated.EventType;

import static org.junit.Assert.*;
import org.junit.Test;

public class EventRegistryCleanerTest {

	@Test
	public void ensureCacheCleanerCleansExpiredClients() throws Exception {
		EventRegistry registry = new TestEventRegistry();
		String c1 = registry.registerClient();
		String c2 = registry.registerClient();

		registry.addEvent(EventFactory.createWatchedSeriesAddedEvent(null));
		assertEquals(1, registry.getEvents(c1).size());
		assertEquals(1, registry.getEvents(c2).size());

		EventRegistryCleaner cleaner = new EventRegistryCleaner(registry, 0, 0);
		cleaner.start();

		// Don't like having sleeps in tests (yuck), but until
		// I find time to look into it further, a 1 millisecond
		// delay is not too bad.
		Thread.sleep(1);

		assertFalse(registry.isRegistered(c1));
		assertFalse(registry.isRegistered(c2));
		cleaner.kill();
	}

	private class TestEventRegistry extends EventRegistry {

		protected TestEventRegistry() {
			super(new ClientIdGenerator());
		}

	}
}
