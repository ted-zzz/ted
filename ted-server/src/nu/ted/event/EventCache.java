package nu.ted.event;

import java.util.ArrayList;
import java.util.List;

import nu.ted.generated.Event;

public class EventCache {
	private List<Event> events;
	private long lastPollTime;

	public EventCache() {
		events = new ArrayList<Event>();
		updateLastPollTime();
	}

	public void clear() {
		events.clear();
	}

	public List<Event> getEvents() {
		updateLastPollTime();
		return new ArrayList<Event>(events);
	}


	public void addEvent(Event event) {
		events.add(event);
	}

	public long getLastPollTime() {
		return lastPollTime;
	}

	private void updateLastPollTime() {
		lastPollTime = System.currentTimeMillis();
	}
}
