package nu.ted;

import static org.junit.Assert.*;

import nu.ted.generated.Ted;

import org.junit.Test;

// Test around serializeTed
// They likely don't actually belong in the Server, so this will be moved later.
public class ServerTest {

	@Test
	public void simpleObjectRoundTripShouldWork()
	{
		Ted ted = Server.createDefaultTed();
		ted.getConfig().setPort((short) 42);

		byte[] bytes = Server.serializeTed(ted);

		// Just to peek with a debugger:
		String encoded = new String(bytes);
		assertNotNull(encoded);

		Ted after = Server.deserializeTed(bytes);
		assertEquals((short) 42, after.getConfig().getPort());
	}

}
