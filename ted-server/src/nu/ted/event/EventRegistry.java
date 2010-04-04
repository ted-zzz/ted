package nu.ted.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventRegistry {

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
		String clientId = this.idGenerator.generateClientId();
		registry.put(clientId, new ArrayList<String>());
		return clientId;
	}

	public boolean isRegistered(String clientId) {
		return registry.containsKey(clientId);
	}

	private void addClientId(String clientId) {
		
	}
}
