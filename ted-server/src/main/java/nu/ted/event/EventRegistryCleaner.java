package nu.ted.event;

public class EventRegistryCleaner extends Thread {
	private long millisToWaitBeforeClean;
	private int maxEventAgeInMinutes;
	private EventRegistry eventRegistry;
	private boolean process = true;

	public EventRegistryCleaner(EventRegistry eventRegistry,
								long millisToWaitBeforeClean,
								int maxEventAgeInMinutes) {
		this.setDaemon(true);
		this.setName("TED [EVENT CACHE CLEANER]");
		this.eventRegistry = eventRegistry;
		this.millisToWaitBeforeClean = millisToWaitBeforeClean;
		this.maxEventAgeInMinutes = maxEventAgeInMinutes;
	}

	@Override
	public void run() {
		while(process) {
			try {
				sleep(millisToWaitBeforeClean);
				eventRegistry.cleanup(maxEventAgeInMinutes);
			}
			catch (InterruptedException e) {}
		}
	}

	public void kill() {
		this.process = false;
	}

}
