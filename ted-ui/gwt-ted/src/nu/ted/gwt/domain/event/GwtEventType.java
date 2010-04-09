package nu.ted.gwt.domain.event;

import java.io.Serializable;
/**
 *	Event types that are direct copies of those from the TED API. This
 *	class is needed (even though the generated one is valid for GWT) b/c
 *	we would need to isolate the class to a single package that only contained
 *	GWT compatible classes. Also the src would have to be included.
 *
 *	Since we want to keep any concept of GWT out of the API, we'll leave it.
 *
 *  <strong>NOTE that any entry added here, must match the entry in the TED API
 *  <code>Event</code> class.</strong>
 */
public enum GwtEventType implements Serializable {

	WATCHED_SERIES_ADDED,
	WATCHED_SERIES_REMOVED;

}
