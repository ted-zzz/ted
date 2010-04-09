package nu.ted.gwt.client.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nu.ted.gwt.client.MessageCallback;
import nu.ted.gwt.domain.event.GwtEvent;
import nu.ted.gwt.domain.event.GwtEventType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;

public class EventQueue{

	// TODO: Make time configurable.
	private static final int RETRY_DELAY = 8000;

	private static final EventServiceAsync EVENT_SERVICE = GWT.create(EventService.class);

	private Map<GwtEventType, List<EventListener<GwtEvent>>> queue;
	private String clientId;

	private EventQueue() {
		queue = new HashMap<GwtEventType, List<EventListener<GwtEvent>>>();
		start();
	}

	private void addListener(GwtEventType type, EventListener<GwtEvent> listener) {
		if (!queue.containsKey(type)) {
			List<EventListener<GwtEvent>> listeners = new ArrayList<EventListener<GwtEvent>>();
			listeners.add(listener);
			queue.put(type, listeners);
		}
		else {
			List<EventListener<GwtEvent>> listeners = queue.get(type);
			listeners.add(listener);
		}
	}

	private void start() {
		EVENT_SERVICE.registerForEvents(new MessageCallback<String>() {

			@Override
			public void onSuccess(final String clientId) {
				setClientId(clientId);

				Timer timer = new Timer() {

					@Override
					public void run() {
						System.out.println("Checking for events with client ID :" + clientId);
						checkForEvents();
					}
				};
				timer.scheduleRepeating(RETRY_DELAY);
			}

		});
	}

	private void checkForEvents() {
		EVENT_SERVICE.getEvents(clientId, new MessageCallback<List<GwtEvent>>() {

			@Override
			public void onSuccess(final List<GwtEvent> events) {
				processEvents(events);
			}

		});
	}

	private void processEvents(List<GwtEvent> events) {
		for (GwtEvent e : events) {
			if (!queue.containsKey(e.getType())) {
				// Ignore - No listeners registered for this type of event.
				continue;
			}

			List<EventListener<GwtEvent>> listeners = queue.get(e.getType());
			for (EventListener<GwtEvent> l : listeners) {
				l.onEvent(e);
			}
		}
	}

	private void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@SuppressWarnings("unchecked")
	public static
	void registerListener(GwtEventType type, EventListener<? extends GwtEvent> listener) {
		// TODO [MS] Look into why this cast is necessary. YUCK!
		InstanceHolder.INSTANCE.addListener(type, (EventListener<GwtEvent>) listener);
	}

	private static class InstanceHolder {
		private static final EventQueue INSTANCE = new EventQueue();
	}

}
