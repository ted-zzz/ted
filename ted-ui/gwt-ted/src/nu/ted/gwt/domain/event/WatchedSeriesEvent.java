package nu.ted.gwt.domain.event;

import nu.ted.gwt.domain.GwtWatchedSeries;

public class WatchedSeriesEvent extends GwtEvent {

	private GwtWatchedSeries series;

	// Required by GWT.
	protected WatchedSeriesEvent() {}

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
