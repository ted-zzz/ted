package nu.ted.gwt.server.event;

import nu.ted.gwt.domain.GwtWatchedSeries;
import nu.ted.gwt.domain.event.GwtEvent;
import nu.ted.gwt.domain.event.GwtEventType;

public class WatchedSeriesEvent extends GwtEvent {

	private GwtWatchedSeries series;

	public WatchedSeriesEvent(GwtEventType type, GwtWatchedSeries series) {
		super(type);
		this.series = series;
	}

	public GwtWatchedSeries getSeries() {
		return series;
	}

	public void setSeries(GwtWatchedSeries series) {
		this.series = series;
	}
}
