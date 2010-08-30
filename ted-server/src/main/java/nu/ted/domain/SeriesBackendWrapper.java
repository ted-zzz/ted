package nu.ted.domain;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.sun.org.apache.xml.internal.utils.UnImplNode;

import nu.ted.event.EventFactory;
import nu.ted.generated.Date;
import nu.ted.generated.Episode;
import nu.ted.generated.EpisodeStatus;
import nu.ted.generated.Event;
import nu.ted.generated.EventType;
import nu.ted.generated.Series;
import nu.ted.generated.TorrentSource;
import nu.ted.generated.TorrentSourceUnion;
import nu.ted.guide.DataSourceException;
import nu.ted.guide.GuideDB;
import nu.ted.guide.GuideFactory;
import nu.ted.service.TedServiceImpl;
import nu.ted.torrent.search.TorrentSourceTypeIndex;
import nu.ted.torrent.search.TorrentSourceType;

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
		if (series.getGuideName() != other.getGuideName())
			return false;

		if (series.getGuideId() != other.getGuideId())
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

	private List<TorrentSource> getTorrentSources() {
		TorrentSourceUnion tsu = series.getSources();

		if (tsu.getName() != null) {
			// TODO: fix this
			throw new UnsupportedOperationException("Not yet complete, only works with sources directly on series");
		} else {
			return tsu.getSources();
		}
	}

	public void searchForMissingEpisodes() {

		if (!hasMissingEpisodes())
			return;

		List<TorrentSource> sources = getTorrentSources();

		List<Episode> missings = new LinkedList<Episode>();
		for (Episode e : series.getEpisodes()) {
			if (e.getStatus() == EpisodeStatus.SEARCHING)
				missings.add(e);
		}

		for (Episode e : missings) {
			for (TorrentSource source : sources) {
				TorrentSourceTypeIndex.getTorrentSourceType(source.getType()).searchEpisode(e);
			}

		}
	}
}
