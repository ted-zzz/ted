package nu.ted.event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nu.ted.generated.Event;

public class EventRegistry {

	private Logger logger = LoggerFactory.getLogger(EventRegistry.class);

	private List<RegisteredEvent> events;

	public static EventRegistry createEventRegistry(long cleanIntervalInMillis, int maxEventAgeInMinutes) {
		EventRegistry registry = new EventRegistry();
		EventRegistryCleaner cleaner = new EventRegistryCleaner(registry, cleanIntervalInMillis, maxEventAgeInMinutes);
		cleaner.start();
		return registry;
	}

	protected EventRegistry() {
		this.events = new ArrayList<RegisteredEvent>();
	}

	public void addEvent(Event event) {
		synchronized (this.events) {
			this.events.add(createRegisteredEvent(event));
		}
	}

	protected RegisteredEvent createRegisteredEvent(Event event) {
		return new RegisteredEvent(event, new Date());
	}

	public List<Event> getEvents(Date from) {
		synchronized (events) {
			List<Event> matched = new ArrayList<Event>();
			for (RegisteredEvent regEvent : events) {
				if (!regEvent.getRegisteredOn().before(from)) {
					matched.add(regEvent.getEvent());
				}
			}

			return matched;
		}
	}

	public void cleanup(int minutesOld) {
		synchronized (events) {
			List<RegisteredEvent> toRemove = new ArrayList();
			for (RegisteredEvent regEvent : events) {
				long threshold = 60000 * minutesOld; // Minutes to millis.

				Calendar cal = Calendar.getInstance();
				long currentMillis = cal.getTimeInMillis();

				cal.setTime(regEvent.getRegisteredOn());
				long eventMillis = cal.getTimeInMillis();

				if ((currentMillis - eventMillis) > threshold) {
					toRemove.add(regEvent);
				}
			}
			events.removeAll(toRemove);
		}
	}

}
