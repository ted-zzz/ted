package nu.ted.domain;

import java.util.Calendar;
import java.util.List;

import nu.ted.generated.Episode;
import nu.ted.generated.EpisodeStatus;
import nu.ted.generated.Series;
import nu.ted.guide.DataSourceException;
import nu.ted.guide.GuideDB;
import nu.ted.guide.GuideFactory;

/**
 * This is a representation of a TV Series.
 *
 */
public class SeriesBackendWrapper
{
	Series series;

	public SeriesBackendWrapper(Series series) {
		this.series = series;
	}

	public Episode getLastEpisode() {
		if (series.getEpisodesSize() > 0)
			return series.getEpisodes().get(series.getEpisodesSize() - 1);
		return null; /* TODO: what if no last */
	}

	public GuideDB getGuide() {
		String guideName = series.getGuideName();
		return GuideFactory.getGuide(guideName);

	}

	public void update(Calendar calendar) {
		Episode last = getLastEpisode();
		GuideDB guide = getGuide();
		String guideId = series.getGuideId();

		List<Episode> newEpisodes;
		try {
			newEpisodes = guide.getNewAiredEpisodes(guideId, calendar, last);
			for (Episode e : newEpisodes) {
				// TODO: need a better way to enforce Status is set here.
				e.setStatus(EpisodeStatus.SEARCHING);
				series.addToEpisodes(e);
			}
		} catch (DataSourceException e1) {
			// TODO: log or rethrow?
		}

	}
}
