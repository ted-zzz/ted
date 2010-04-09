package nu.ted.gwt.client.event;

import java.util.ArrayList;
import java.util.List;

import nu.ted.gwt.client.MessageCallback;
import nu.ted.gwt.domain.GwtEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;

public class EventQueue {

	private static final EventServiceAsync EVENT_SERVICE = GWT.create(EventService.class);

	private List<EventListener> listeners;
	private String clientId;

	private EventQueue() {
		listeners = new ArrayList<EventListener>();
		start();
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
				timer.scheduleRepeating(8000);
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
			for (EventListener l : listeners) {
				l.onEvent();
			}
		}
	}

	private void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public static void addListener(EventListener listener) {
		InstanceHolder.INSTANCE.listeners.add(listener);
	}

	private static class InstanceHolder {
		private static final EventQueue INSTANCE = new EventQueue();
	}

}
