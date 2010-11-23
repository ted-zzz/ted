package nu.ted.domain;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import nu.ted.DataRetrievalException;
import nu.ted.event.EventFactory;
import nu.ted.generated.TDate;
import nu.ted.generated.Episode;
import nu.ted.generated.EpisodeStatus;
import nu.ted.generated.Series;
import nu.ted.guide.GuideDB;
import nu.ted.guide.GuideFactory;
import nu.ted.service.TedServiceImpl;

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

	public boolean isSameSeries(Series other) {
		if (!series.getGuideName().equals(other.getGuideName()))
			return false;

		if (!series.getGuideId().equals(other.getGuideId()))
			return false;

		return true;
	}

	public boolean update(Calendar calendar) {
		boolean foundNew = false;
		TDate after = series.getLastCheck();
		TDate before = new TDate(calendar.getTimeInMillis());

		GuideDB guide = getGuide();
		String guideId = series.getGuideId();

		List<Episode> newEpisodes;
		try {
			newEpisodes = guide.getAiredEpisodesBetween(guideId, after, before);
			for (Episode e : newEpisodes) {
				// TODO: need a better way to enforce Status is set here.
				e.setStatus(EpisodeStatus.SEARCHING);
				series.addToEpisodes(e);
				foundNew = true;
				TedServiceImpl.registerEvent(EventFactory.createEpisodeAddedEvent(series, e));
			}
			series.setLastCheck(before);
		} catch (DataRetrievalException e1) {
			// TODO: log or rethrow?
		}
		return foundNew;

	}

	public boolean hasMissingEpisodes() {
		for (Episode e : series.getEpisodes()) {
			if (e.getStatus() == EpisodeStatus.SEARCHING)
				return true;
		}
		return false;
	}

	public List<Episode> getMissingEpisodes() {
		List<Episode> episodes = new LinkedList<Episode>();
		for (Episode e : series.getEpisodes()) {
			if (e.getStatus() == EpisodeStatus.SEARCHING) {
				episodes.add(e);
			}
		}
		return episodes;
	}


	/**
	 * For now just split the terms on space. Could be configurable later.
	 */
	public List<String> getSearchTerms() {
		return Arrays.asList(series.getName().split(" "));
	}
}
