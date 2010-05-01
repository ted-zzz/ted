package nu.ted.guide;

/**
 * Base exception that the datasource might throw.
 *
 */
public class DataSourceException extends Exception {

	private static final long serialVersionUID = 1L;

	public DataSourceException() {
		super();
	}

	public DataSourceException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataSourceException(String message) {
		super(message);
	}

	public DataSourceException(Throwable cause) {
		super(cause);
	}

}
