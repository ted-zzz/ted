package nu.ted.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class EventRegistryTest {

	@Test
	public void ensureGetGeneratedKeyOnRegistration() {
		String clientId = "C1-GWT";

		TestClientIdGenerator idGenerator = new TestClientIdGenerator();
		EventRegistry registry = new EventRegistry(idGenerator);
		
		idGenerator.setNextExpectedId(clientId);
		String registryKey = registry.registerClient();
		assertEquals(clientId, registryKey);
	}
	
	@Test
	public void registeringClientAddsClientIdToKeyList() {
		String expectedClientId = "C1-GWT";

		TestClientIdGenerator idGenerator = new TestClientIdGenerator();
		EventRegistry registry = new EventRegistry(idGenerator);
		
		idGenerator.setNextExpectedId(expectedClientId);
		String clientId = registry.registerClient();
		assertTrue("Expected registry to contain client key: " + clientId, registry.isRegistered(clientId));
	}
	
	private class TestClientIdGenerator extends ClientIdGenerator {
		private String nextId;

		@Override
		public String generateClientId() {
			if (nextId == null) {
				fail("Next ID was not set in TestClientIdGenerator.");
			}
			String nextIdCopy = nextId;
			nextId = null;
			return nextIdCopy;
		}
		
		public void setNextExpectedId(String id) {
			nextId = id;
		}
		
	}

}
