package nu.ted.event;

import java.math.BigInteger;
import java.security.SecureRandom;

public class ClientIdGenerator {
	
	private SecureRandom random;

	public ClientIdGenerator() {
		random = new SecureRandom();
	}
	
	public String generateClientId() {
		return new BigInteger(120, random).toString(32);
	}
}
