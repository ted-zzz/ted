package nu.ted.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

import nu.ted.client.ClientIdGenerator;

import org.junit.Test;

public class ClientIdGeneratorTest {

	@Test
	public void ensureClientIdGeneration() {

		// Use static seed to always have consistent results.
		byte[] seed = "abc".getBytes();
		ClientIdGenerator generator = new ClientIdGenerator(seed);

		String generatedClientId = generator.generateClientId();
		assertNotNull("Id generator failed to generate and Id.", generatedClientId);
		assertFalse("Generated ID was empty", generatedClientId.isEmpty());

		String secondId = generator.generateClientId();
		assertFalse(generatedClientId.equals(secondId));
	}

}
