package nu.ted.event;

public class EventCacheCleaner extends Thread {
	private long retry;
	private long idleLimit;
	private EventRegistry eventRegistry;
	private boolean process = true;

	public EventCacheCleaner(EventRegistry eventRegistry, long retry, long idleLimit) {
		this.setDaemon(true);
		this.setName("TED [EVENT CACHE CLEANER]");
		this.eventRegistry = eventRegistry;
		this.retry = retry;
		this.idleLimit = idleLimit;
	}

	@Override
	public void run() {
		while(true) {
			try {
				sleep(retry);
				for (String id : eventRegistry.getClientIds()) {
					long pollTime = eventRegistry.getLastPollTime(id);
					long currentTime = System.currentTimeMillis();
					if ((currentTime - pollTime) > idleLimit) {
						eventRegistry.unregisterClient(id);
					}
				}
			}
			catch (InterruptedException e) {}
		}
	}

	public void kill() {
		this.process = false;
	}

}
