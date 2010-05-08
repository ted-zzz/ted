package nu.ted.event;

import nu.ted.generated.Episode;
import nu.ted.generated.Event;
import nu.ted.generated.EventType;
import nu.ted.generated.Series;

public class EventFactory {

	public static Event createWatchedSeriesAddedEvent(Series addedSeries) {
		Event watchedSeriesAddedEvent = new Event();
		watchedSeriesAddedEvent.setType(EventType.WATCHED_SERIES_ADDED);
		watchedSeriesAddedEvent.setSeries(addedSeries);
		return watchedSeriesAddedEvent;
	}

	public static Event createWatchedSeriesRemovedEvent(Series removedSeries) {
		Event watchedSeriesRemovedEvent = new Event();
		watchedSeriesRemovedEvent.setType(EventType.WATCHED_SERIES_REMOVED);
		watchedSeriesRemovedEvent.setSeries(removedSeries);
		return watchedSeriesRemovedEvent;
	}

	public static Event createEpisodeAddedEvent(Series owner, Episode addedEpisode) {
		return new Event(EventType.EPISODE_ADDED, owner, addedEpisode);
	}
}
