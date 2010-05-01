package nu.ted.event;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ClientIdGeneratorTest {

	@Test
	public void ensureClientIdGeneration() {
		ClientIdGenerator generator = new ClientIdGenerator();
		String generatedClientId = generator.generateClientId();
		System.out.println("Generated client ID was: " + generatedClientId);
		assertNotNull("Id generator failed to generate and Id.", generatedClientId);
		assertFalse("Generated ID was empty", generatedClientId.isEmpty());
	}
	
}
