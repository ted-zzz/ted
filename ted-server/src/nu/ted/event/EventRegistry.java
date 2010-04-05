package nu.ted.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nu.ted.generated.Event;

// TODO [MS] Still need a way of removing a client if it does not poll after a
// 			 certain time period.
public class EventRegistry {

	protected static final int MAX_TRIES = 50;

	private ClientIdGenerator idGenerator;
	private Map<String, List<Event>> registry;

	public EventRegistry() {
		this(new ClientIdGenerator());
	}

	public EventRegistry(ClientIdGenerator clientIdGenerator) {
		this.idGenerator = clientIdGenerator;
		this.registry = new HashMap<String, List<Event >>();
	}

	public String registerClient() {
		synchronized(registry) {
			String generatedId = null;;
			int count = 1;

			// Adding a max tries just to be certain that if the generator ever
			// messed up, we don't get caught in an infinite loop.
			//
			// NOTE: THIS IS NOT LIKELY TO EVER HAPPEN (BETTER SAFE THAN SORRY).
			while(count != MAX_TRIES) {
				generatedId = this.idGenerator.generateClientId();
				if (registry.containsKey(generatedId)) {
					count++;
					continue;
				}
				break;
			}

			if (count == MAX_TRIES) {
				// TODO [MS] Throw properly typed exception here?
				throw new RuntimeException("Unable to generate unique client ID for Client.");
			}

			registry.put(generatedId, new ArrayList<Event>());
			return generatedId;
		}
	}

	public boolean isRegistered(String clientId) {
		synchronized(registry) {
			return registry.containsKey(clientId);
		}
	}

	public void addEvent(Event event) {
		synchronized(registry) {
			for (Entry<String, List<Event>> entry: registry.entrySet()) {
				entry.getValue().add(event);
			}
		}
	}

	public List<Event> getEvents(String clientId) {
		synchronized(registry) {
			try {
				if (!isRegistered(clientId)) {
					// TODO [MS] Throw properly typed exception here?
					throw new RuntimeException("Unable to get events. Client not registered: " +
							clientId);
				}

				return registry.get(clientId);
			}
			finally {
				// Event list is cleared after a call to get.
				registry.put(clientId, new ArrayList<Event>());
			}
		}
	}


}
