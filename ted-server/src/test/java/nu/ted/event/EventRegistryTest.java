package nu.ted.event;

import static org.junit.Assert.*;

import java.util.List;

import nu.ted.generated.Event;
import nu.ted.generated.EventType;

import org.junit.Test;

public class EventRegistryTest {

	@Test
	public void registeringClientAddsClientIdToKeyList() {
		String expectedClientId = "C1-GWT";

		EventRegistry registry = EventRegistry.createEventRegistry(5000L, 5000L);

		registry.registerClient(expectedClientId);
		assertTrue("Expected registry to contain client key: " + expectedClientId,
				registry.isRegistered(expectedClientId));
	}

	@Test
	public void checkEventAssociatedWithAllRegisteredClients() {
		EventRegistry registry = EventRegistry.createEventRegistry(5000L, 5000L);

		String clientId1 = "CID_1";
		String clientId2 = "CID_2";

		registry.registerClient(clientId1);
		registry.registerClient(clientId2);

		Event watchedListChanged = EventFactory.createWatchedSeriesAddedEvent(null);
		registry.addEvent(watchedListChanged);

		List<Event> client1Events = registry.getEvents(clientId1);
		assertEquals(1, client1Events.size());
		assertEquals(watchedListChanged, client1Events.get(0));

		List<Event>	client2Events = registry.getEvents(clientId2);
		assertEquals(1, client2Events.size());
		assertEquals(watchedListChanged, client2Events.get(0));
	}

	@Test(expected = RuntimeException.class)
	public void ensureExceptionIsThrownIfClientIsNotRegisteredOnGetEvents() {
		EventRegistry registry = EventRegistry.createEventRegistry(5000L, 5000L);
		registry.getEvents("SS");
	}

	@Test
	public void ensureEventListIsClearedAfterRetrieval() {
		EventRegistry registry = EventRegistry.createEventRegistry(5000L, 5000L);

		String clientId1 = "CID_1";

		registry.registerClient(clientId1);

		Event watchedListChanged = EventFactory.createWatchedSeriesAddedEvent(null);
		registry.addEvent(watchedListChanged);

		List<Event> client1Events = registry.getEvents(clientId1);
		assertEquals(1, client1Events.size());
		assertEquals(watchedListChanged, client1Events.get(0));

		assertTrue("Events should be empty after get.",
				registry.getEvents(clientId1).isEmpty());
	}

	@Test
	public void ensureGetLastPollTimeFromClient() {
		EventRegistry registry = EventRegistry.createEventRegistry(5000L, 5000L);

		String clientId1 = "CID_1";
		registry.registerClient(clientId1);

		Event watchedListChanged = EventFactory.createWatchedSeriesAddedEvent(null);
		registry.addEvent(watchedListChanged);

		assertFalse(0L == registry.getLastPollTime(clientId1));
	}

	@Test(expected = RuntimeException.class)
	public void ensureGetLastPollTimeFromClientThrowsExceptionIfClientDoesNotExist() {
		EventRegistry registry = EventRegistry.createEventRegistry(5000L, 5000L);
		registry.getLastPollTime("Unknown Client");
	}

}
