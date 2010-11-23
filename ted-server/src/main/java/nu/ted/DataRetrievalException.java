package nu.ted;

/**
 * Base exception that might occur while accessing data.
 *
 */
public class DataRetrievalException extends Exception {

	private static final long serialVersionUID = 1L;

	public DataRetrievalException() {
		super();
	}

	public DataRetrievalException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataRetrievalException(String message) {
		super(message);
	}

	public DataRetrievalException(Throwable cause) {
		super(cause);
	}

}
