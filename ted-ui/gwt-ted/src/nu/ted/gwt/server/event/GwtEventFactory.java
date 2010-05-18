package nu.ted.gwt.server.event;

import nu.ted.generated.Event;
import nu.ted.generated.EventType;
import nu.ted.generated.Series;
import nu.ted.gwt.domain.GwtWatchedSeries;
import nu.ted.gwt.domain.event.EpisodeAddedEvent;
import nu.ted.gwt.domain.event.GwtEvent;
import nu.ted.gwt.domain.event.GwtEventType;
import nu.ted.gwt.domain.event.WatchedSeriesEvent;
import nu.ted.gwt.server.page.watched.GwtTedObjectFactory;

/**
 * Responsible for converting a TED API event into a GwtEvent.
 *
 */
public class GwtEventFactory {

	public GwtEvent createEvent(Event tedServerEvent) {
		EventType type = tedServerEvent.getType();
		if (EventType.WATCHED_SERIES_ADDED.equals(type) ||
			EventType.WATCHED_SERIES_REMOVED.equals(type)) {
			return createWatchedSeriesEvent(tedServerEvent);
		}
		else if (EventType.EPISODE_ADDED.equals(type)) {
			return createEpisodeAddedEvent(tedServerEvent);
		}

		// TODO [MS] Throw appropriate exception here.
		throw new RuntimeException("Unknow event retrieved from server. " +
				"Could not generate GWT equivillant.");
	}

	private WatchedSeriesEvent createWatchedSeriesEvent(Event tedServerEvent) {
		Series series = tedServerEvent.getSeries();
		return new WatchedSeriesEvent(GwtEventType.valueOf(tedServerEvent.getType().name()),
				GwtTedObjectFactory.create(series));
	}

	private EpisodeAddedEvent createEpisodeAddedEvent(Event tedServerEvent) {
		return new EpisodeAddedEvent(GwtEventType.valueOf(tedServerEvent.getType().name()),
				GwtTedObjectFactory.create(tedServerEvent.getSeries()),
				GwtTedObjectFactory.create(tedServerEvent.getEpisode()));
	}

}
