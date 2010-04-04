package nu.ted.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventRegistry {

	protected static final int MAX_TRIES = 50;
	
	private ClientIdGenerator idGenerator;
	private Map<String, List<String>> registry;
	
	public EventRegistry() {
		this(new ClientIdGenerator());
	}
	
	public EventRegistry(ClientIdGenerator clientIdGenerator) {
		this.idGenerator = clientIdGenerator;
		this.registry = new HashMap<String, List<String>>();
	}
	
	public String registerClient() {		
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
		
		registry.put(generatedId, new ArrayList<String>());
		return generatedId;
	}

	public boolean isRegistered(String clientId) {
		return registry.containsKey(clientId);
	}

	
}
