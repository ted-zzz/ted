package nu.ted.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nu.ted.generated.Event;

public class EventRegistry {

	protected static final int MAX_TRIES = 50;

	private ClientIdGenerator idGenerator;
	private Map<String, EventCache> registry;

	public EventRegistry(long maxClientIdleTime, long wait) {
		this(new ClientIdGenerator(), maxClientIdleTime, wait);
	}

	public EventRegistry(ClientIdGenerator clientIdGenerator, long maxClientIdleTime, long wait) {
		this.idGenerator = clientIdGenerator;
		this.registry = new HashMap<String, EventCache>();
		startRegistryCleaner(maxClientIdleTime, wait);
	}

	protected void startRegistryCleaner(long maxClientIdleTime, long wait) {
		EventRegistryCleaner cleaner = new EventRegistryCleaner(this, wait,
				maxClientIdleTime);
		cleaner.start();
	}

	public String registerClient() {
		synchronized (registry) {
			String generatedId = null;
			;
			int count = 1;

			// Adding a max tries just to be certain that if the generator ever
			// messed up, we don't get caught in an infinite loop.
			//
			// NOTE: THIS IS NOT LIKELY TO EVER HAPPEN (BETTER SAFE THAN SORRY).
			while (count != MAX_TRIES) {
				generatedId = this.idGenerator.generateClientId();
				if (registry.containsKey(generatedId)) {
					count++;
					continue;
				}
				break;
			}

			if (count == MAX_TRIES) {
				// TODO [MS] Throw properly typed exception here?
				throw new RuntimeException(
						"Unable to generate unique client ID for Client.");
			}

			registry.put(generatedId, new EventCache());
			return generatedId;
		}
	}

	public boolean isRegistered(String clientId) {
		synchronized (registry) {
			return registry.containsKey(clientId);
		}
	}

	public void addEvent(Event event) {
		synchronized (registry) {
			for (Entry<String, EventCache> entry : registry.entrySet()) {
				entry.getValue().addEvent(event);
			}
		}
	}

	public List<Event> getEvents(String clientId) {
		synchronized (registry) {
			if (!isRegistered(clientId)) {
				// TODO [MS] Throw properly typed exception here?
				throw new RuntimeException(
						"Unable to get events. Client not registered: "
								+ clientId);
			}

			EventCache cache = registry.get(clientId);
			try {
				return cache.getEvents();
			} finally {
				cache.clear();
			}
		}
	}

	public String[] getClientIds() {
		synchronized (registry) {
			return registry.keySet().toArray(
					new String[registry.keySet().size()]);
		}
	}

	public long getLastPollTime(String clientId) {
		synchronized (registry) {
			if (!isRegistered(clientId)) {
				// TODO [MS] Throw properly typed exception here?
				throw new RuntimeException("Client not registered: " + clientId);
			}
			return registry.get(clientId).getLastPollTime();
		}
	}

	public void unregisterClient(String clientId) {
		synchronized (registry) {
			registry.remove(clientId);
		}
	}

}
