package nu.ted.event;

import java.util.Date;

import nu.ted.generated.Event;

public class TestEventRegistry extends EventRegistry {
	private Date nextRegisterDate;

	protected TestEventRegistry() {
		super();
	}

	@Override
	protected RegisteredEvent createRegisteredEvent(Event event) {
		return new RegisteredEvent(event, nextRegisterDate);
	}

	public void setNextRegisterDate(Date next) {
		this.nextRegisterDate = next;
	}
}
