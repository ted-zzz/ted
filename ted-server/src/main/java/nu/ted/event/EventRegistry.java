package nu.ted.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nu.ted.generated.Event;

public class EventRegistry {

	private Logger logger = LoggerFactory.getLogger(EventRegistry.class);

	protected static final int MAX_TRIES = 50;

	private Map<String, EventCache> registry;

	public static EventRegistry createEventRegistry(long maxClientIdleTime, long wait) {
		EventRegistry registry = new EventRegistry();
		return registry;
	}

	protected EventRegistry() {
		this.registry = new HashMap<String, EventCache>();
	}

	public void registerClient(String Id) {
		synchronized (registry) {
			registry.put(Id, new EventCache());
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
			EventCache removed = registry.remove(clientId);
			if (removed != null) {
				logger.debug("Client Unrigistered: {}", clientId);
			}
		}
	}

}
