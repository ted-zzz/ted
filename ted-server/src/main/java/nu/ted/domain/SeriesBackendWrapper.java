package nu.ted.domain;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import nu.ted.event.EventFactory;
import nu.ted.generated.Date;
import nu.ted.generated.Episode;
import nu.ted.generated.EpisodeStatus;
import nu.ted.generated.Series;
import nu.ted.generated.TorrentSource;
import nu.ted.guide.DataSourceException;
import nu.ted.guide.GuideDB;
import nu.ted.guide.GuideFactory;
import nu.ted.service.TedServiceImpl;
import nu.ted.torrent.TorrentRef;
import nu.ted.torrent.search.TorrentSourceType;
import nu.ted.torrent.search.TorrentSourceIndex;

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
		Date after = series.getLastCheck();
		Date before = new Date(calendar.getTimeInMillis());

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
		} catch (DataSourceException e1) {
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

	public void searchForMissingEpisodes(List<TorrentSource> sources) {

		if (!hasMissingEpisodes())
			return;

		List<Episode> missings = new LinkedList<Episode>();
		for (Episode e : series.getEpisodes()) {
			if (e.getStatus() == EpisodeStatus.SEARCHING)
				missings.add(e);
		}

		for (Episode e : missings) {
			for (TorrentSource source : sources) {
				TorrentSourceType torrentSourceType =
					TorrentSourceIndex.getTorrentSourceType(source);
				List<TorrentRef> torrents = torrentSourceType.searchEpisode(this, e);

				if (!torrents.isEmpty()) {
					for (TorrentRef t : torrents) {
						System.out.println("Found torrent title: " + t.getTitle() + " link: " + t.getLink());
					}
					// TODO: evaluate the torrents
				}
			}

		}
	}

	/**
	 * For now just split the terms on space. Could be configurable later.
	 */
	public String[] getSearchTerms() {
		return series.getName().split(" ");
	}
}
