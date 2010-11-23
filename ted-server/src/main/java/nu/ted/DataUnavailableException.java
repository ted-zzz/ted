package nu.ted;


/**
 * Your data is missing, perhaps it doesn't exist on the record.
 *
 */
public class DataUnavailableException extends DataRetrievalException
{
	private static final long serialVersionUID = 1L;

	public DataUnavailableException() {
		super();
	}

	public DataUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataUnavailableException(String message) {
		super(message);
	}

	public DataUnavailableException(Throwable cause) {
		super(cause);
	}

}
