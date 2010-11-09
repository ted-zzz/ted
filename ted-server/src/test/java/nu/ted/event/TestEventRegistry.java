package nu.ted.event;

import java.util.Calendar;
import java.util.Date;

import nu.ted.generated.Event;

public class TestEventRegistry extends EventRegistry {
	private Date nextRegisterDate;

	protected TestEventRegistry() {
		super();
	}

	@Override
	protected Calendar getRegisteredDateCalendar() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(nextRegisterDate);
		return cal;
	}

	public void setNextRegisterDate(Date next) {
		this.nextRegisterDate = next;
	}
}
