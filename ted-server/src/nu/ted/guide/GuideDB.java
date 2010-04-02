package nu.ted.guide;

import java.util.Calendar;
import java.util.List;

import nu.ted.generated.Episode;
import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.Series;
import nu.ted.generated.SeriesSearchResult;

public interface GuideDB
{
	// Get the name for this guide.
	String getName();

	List<SeriesSearchResult> search(String name) throws DataSourceException;

	Series getSeries(String guideId, short id, Calendar date) throws DataSourceException;

	String getName(String guideId) throws DataSourceException;

	List<Episode> getNewAiredEpisodes(String guideId, Calendar date, Episode lastEpisode) throws DataSourceException;

	String getOverview(String guideId) throws DataSourceException;

	ImageFile getImage(String guideId, ImageType type) throws DataSourceException;

}
