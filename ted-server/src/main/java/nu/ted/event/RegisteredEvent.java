package nu.ted.event;

import java.util.Date;

import nu.ted.generated.Event;

public class RegisteredEvent {
	private Event event;
	private Date registeredOn;

	public RegisteredEvent(Event event, Date registeredOn) {
		this.event = event;
		this.registeredOn = registeredOn;
	}

	public Event getEvent() {
		return event;
	}

	public Date getRegisteredOn() {
		return registeredOn;
	}

}
