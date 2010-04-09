package nu.ted.gwt.server.page.watched;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nu.ted.generated.Date;
import nu.ted.generated.Episode;
import nu.ted.generated.EpisodeStatus;
import nu.ted.generated.Series;
import nu.ted.gwt.domain.GwtEpisode;
import nu.ted.gwt.domain.GwtEpisodeStatus;
import nu.ted.gwt.domain.GwtWatchedSeries;

public class GwtWatchedSeriesFactory {

	public static GwtWatchedSeries create(Series series) {
		List<GwtEpisode> episodes = createGwtEpisodes(series.getEpisodes());
		return new GwtWatchedSeries(series.getUid(), series.getName(), episodes);
	}

	private static List<GwtEpisode> createGwtEpisodes(List<Episode> episodes) {
		List<GwtEpisode> converted = new ArrayList<GwtEpisode>();
		for (Episode e : episodes) {
			converted.add(new GwtEpisode(e.getSeason(), e.getNumber(),
					convertDate(e.getAired()), convertStatus(e.getStatus())));
		}
		return converted;
	}

	private static GwtEpisodeStatus convertStatus(EpisodeStatus status) {
		return GwtEpisodeStatus.valueOf(status.name());
	}

	private static java.util.Date convertDate(Date airDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(airDate.getValue());
		return cal.getTime();
	}

}
