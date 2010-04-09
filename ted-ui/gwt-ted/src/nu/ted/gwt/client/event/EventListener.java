package nu.ted.gwt.client.event;

import nu.ted.gwt.domain.event.GwtEvent;

public interface EventListener<E extends GwtEvent> {

	void onEvent(E event);

}
