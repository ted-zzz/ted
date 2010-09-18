package nu.ted.client;

import java.math.BigInteger;
import java.security.SecureRandom;

public class ClientIdGenerator {

	private SecureRandom random;

	public ClientIdGenerator() {
		random = new SecureRandom();
	}

	ClientIdGenerator(byte[] seed) {
		random = new SecureRandom(seed);
	}

	/**
	 * Generate a pseudo-unique client id for the user.
	 *
	 * Not guaranteed to be unique, as there's a one in 2.60836176 x 10^52 chance of overlap.
	 */
	public String generateClientId() {
		return new BigInteger(120, random).toString(32);
	}
}
