package nu.ted.gwt.domain.event;

import nu.ted.gwt.domain.GwtEpisode;
import nu.ted.gwt.domain.GwtWatchedSeries;

public class EpisodeAddedEvent extends GwtEvent {

	private static final long serialVersionUID = 1L;

	private GwtWatchedSeries series;
	private GwtEpisode episode;

	// Required by GWT.
	protected EpisodeAddedEvent() {}

	public EpisodeAddedEvent(GwtEventType type, GwtWatchedSeries series, GwtEpisode episode) {
		super(type);
		this.series = series;
		this.episode = episode;
	}

	public GwtWatchedSeries getSeries() {
		return series;
	}

	public void setSeries(GwtWatchedSeries series) {
		this.series = series;
	}

	public void setEpisode(GwtEpisode episode) {
		this.episode = episode;
	}

	public GwtEpisode getEpisode() {
		return this.episode;
	}
}
