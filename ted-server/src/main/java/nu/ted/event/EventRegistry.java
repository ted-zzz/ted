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
import nu.ted.generated.TDate;

public class EventRegistry {

	private Logger logger = LoggerFactory.getLogger(EventRegistry.class);
	private List<Event> events;

	public static EventRegistry createEventRegistry(long cleanIntervalInMillis, int maxEventAgeInMinutes) {
		EventRegistry registry = new EventRegistry();
		EventRegistryCleaner cleaner = new EventRegistryCleaner(registry, cleanIntervalInMillis, maxEventAgeInMinutes);
		cleaner.start();
		return registry;
	}

	protected EventRegistry() {
		this.events = new ArrayList<Event>();
	}

	public void addEvent(Event event) {
		synchronized (this.events) {
			Calendar cal = getRegisteredDateCalendar();
			event.setRegisteredOn(new TDate(cal.getTimeInMillis()));
			this.events.add(event);
		}
	}

	protected Calendar getRegisteredDateCalendar() {
		return Calendar.getInstance();
	}

	public List<Event> getEvents(Date from) {
		synchronized (events) {
			Calendar cal = getRegisteredDateCalendar();

			List<Event> matched = new ArrayList<Event>();
			for (Event regEvent : events) {
				cal.setTimeInMillis(regEvent.getRegisteredOn().getValue());
				Date eventDate = cal.getTime();
				System.err.println(eventDate + " ---> " + from);
				if (eventDate.after(from)) {
					matched.add(regEvent);
				}
			}

			return matched;
		}
	}

	public void cleanup(int minutesOld) {
		synchronized (events) {
			List<Event> toRemove = new ArrayList<Event>();
			for (Event regEvent : events) {
				long threshold = 60000 * minutesOld; // Minutes to millis.

				Calendar cal = Calendar.getInstance();
				long currentMillis = cal.getTimeInMillis();
				long eventMillis = regEvent.getRegisteredOn().getValue();

				if ((currentMillis - eventMillis) > threshold) {
					toRemove.add(regEvent);
				}
			}
			events.removeAll(toRemove);
		}
	}

}
