package nu.ted.event;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import nu.ted.generated.Event;
import nu.ted.generated.EventType;

import org.junit.Test;

public class EventRegistryTest {

	@Test
	public void ensureGetGeneratedKeyOnRegistration() {
		String clientId = "C1-GWT";

		TestClientIdGenerator idGenerator = new TestClientIdGenerator();
		EventRegistry registry = EventRegistry.createEventRegistry(idGenerator, 5000L, 5000L);

		idGenerator.setAvailableIds(clientId);
		String registryKey = registry.registerClient();
		assertEquals(clientId, registryKey);
	}

	@Test
	public void registeringClientAddsClientIdToKeyList() {
		String expectedClientId = "C1-GWT";

		TestClientIdGenerator idGenerator = new TestClientIdGenerator();
		EventRegistry registry = EventRegistry.createEventRegistry(idGenerator, 5000L, 5000L);

		idGenerator.setAvailableIds(expectedClientId);
		String clientId = registry.registerClient();
		assertTrue("Expected registry to contain client key: " + clientId, registry.isRegistered(clientId));
	}

	@Test
	public void ensureUniqueIdIsReturnedOnRegisterClientIfGeneratorHappenedToReturnAnAlreadyRegisteredId() {
		String expectedClientId1 = "C1-GWT";
		String expectedClientId2 = "C2-GWT";

		TestClientIdGenerator idGenerator = new TestClientIdGenerator();
		EventRegistry registry = EventRegistry.createEventRegistry(idGenerator, 5000L, 5000L);

		// Add ID1 twice in a row to simulate that it gets generated again the 2nd time registerClient is called.
		// NOTE that the test generator is implemented using a stack (first in, last out).
		idGenerator.setAvailableIds(expectedClientId2, expectedClientId1, expectedClientId1);
		assertEquals(expectedClientId1, registry.registerClient());
		assertEquals(expectedClientId2, registry.registerClient());
		assertTrue(idGenerator.isEmpty());
	}

	@Test(expected = RuntimeException.class)
	public void ensureTimeoutFoeRegisterClientIfGeneratorNeverGeneratesAUniqueId() {
		String clientId = "C1-GWT";

		// Set the same ID one more than max_attempts so register will time out.
		ArrayList<String> sameIds = new ArrayList<String>();

		// MAX_TRIES + 2: One ID for the initial registration of the ID, and
		//                another to ensure we go over the maximum.
		int max = EventRegistry.MAX_TRIES + 2;
		for(int i = 0; i < max; i++) {
			sameIds.add(clientId);
		}

		TestClientIdGenerator idGenerator = new TestClientIdGenerator();
		idGenerator.setAvailableIds(sameIds.toArray(new String[sameIds.size()]));

		EventRegistry registry = EventRegistry.createEventRegistry(idGenerator, 5000L, 5000L);

		// Ok for the first attempt.
		registry.registerClient();

		// Same ID will be tried until MAX_ATTEMPTS is reached and an exception is thrown.
		registry.registerClient();
	}

	@Test
	public void checkEventAssociatedWithAllRegisteredClients() {
		TestClientIdGenerator idGenerator = new TestClientIdGenerator();
		EventRegistry registry = EventRegistry.createEventRegistry(idGenerator, 5000L, 5000L);

		idGenerator.setAvailableIds("CID_2", "CID_1");

		String clientId1 = registry.registerClient();
		String clientId2 = registry.registerClient();

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
		TestClientIdGenerator generator = new TestClientIdGenerator();
		EventRegistry registry = EventRegistry.createEventRegistry(generator, 5000L, 5000L);
		registry.getEvents("SS");
	}

	@Test
	public void ensureEventListIsClearedAfterRetrieval() {
		TestClientIdGenerator idGenerator = new TestClientIdGenerator();
		EventRegistry registry = EventRegistry.createEventRegistry(idGenerator, 5000L, 5000L);

		idGenerator.setAvailableIds("CID_1");

		String clientId1 = registry.registerClient();

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
		TestClientIdGenerator idGenerator = new TestClientIdGenerator();
		EventRegistry registry = EventRegistry.createEventRegistry(idGenerator, 5000L, 5000L);

		idGenerator.setAvailableIds("CID_1");

		String clientId1 = registry.registerClient();

		Event watchedListChanged = EventFactory.createWatchedSeriesAddedEvent(null);
		registry.addEvent(watchedListChanged);

		assertFalse(0L == registry.getLastPollTime(clientId1));
	}

	@Test(expected = RuntimeException.class)
	public void ensureGetLastPollTimeFromClientThrowsExceptionIfClientDoesNotExist() {
		EventRegistry registry = EventRegistry.createEventRegistry(5000L, 5000L);
		registry.getLastPollTime("Unknown Client");
	}

	private class TestClientIdGenerator extends ClientIdGenerator {
		private Stack<String> available = new Stack<String>();

		@Override
		public String generateClientId() {
			if (available.isEmpty()) {
				fail("Could not get next ID. None available.");
			}
			return available.pop();
		}

		public void setAvailableIds(String ... ids) {
			available.clear();
			available.addAll(Arrays.asList(ids));
		}

		public boolean isEmpty() {
			return available.isEmpty();
		}
	}

}
