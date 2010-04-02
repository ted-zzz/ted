package nu.ted.guide.tvdb;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import nu.ted.generated.Date;
import nu.ted.generated.Episode;
import nu.ted.generated.EpisodeStatus;
import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.Series;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.guide.tvdb.datasource.DataSource;

import nu.ted.guide.DataSourceException;
import nu.ted.guide.GuideDB;

public class TVDB implements GuideDB
{
	public static final String NAME = "TVDB";

	private DataSource dataSource;

	// TODO: might want to singleton up this class, or the GuideFactory might be enough.

	public TVDB(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public String getName() {
		return TVDB.NAME;
	}

	@Override
	public ImageFile getImage(String guideId, ImageType type) throws DataSourceException
	{
		return dataSource.getImage(guideId, type);
	}

	@Override
	public List<SeriesSearchResult> search(String name) throws DataSourceException
	{
		return dataSource.search(name);
	}

	@Override
	public String getOverview(String guideId) throws DataSourceException {
		FullSeriesRecord series = dataSource.getFullSeriesRecord(guideId);
		return series.getOverview();
	}

	@Override
	public List<Episode> getNewAiredEpisodes(String guideId, Calendar date,
			final Episode lastEpisode) throws DataSourceException {

		FullSeriesRecord series = dataSource.getFullSeriesRecord(guideId);
		List<Episode> newOnes = new LinkedList<Episode>();
		Episode last = lastEpisode;
		while (true) {
			Episode e = series.getNextEpisode(last, EpisodeStatus.SEARCHING);
			if (e == null)
				break;
			if (e.getAired().getValue() > date.getTimeInMillis())
				break;
			newOnes.add(e);
			last = e;
		}
		return newOnes;
	}

	@Override
	public Series getSeries(String guideId, short id, Calendar date) throws DataSourceException {
		FullSeriesRecord record = dataSource.getFullSeriesRecord(guideId);

		return new Series(id, record.getName(), new Date(date.getTimeInMillis()),
				getName(), guideId, new LinkedList<Episode>());
	}

	@Override
	public String getName(String guideId) throws DataSourceException {
		return dataSource.getFullSeriesRecord(guideId).getName();
	}
}
