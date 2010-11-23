package nu.ted.guide;

import java.util.Calendar;
import java.util.List;

import nu.ted.DataSourceException;
import nu.ted.generated.TDate;
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

	String getOverview(String guideId) throws DataSourceException;

	ImageFile getImage(String guideId, ImageType type) throws DataSourceException;

	List<Episode> getAiredEpisodesBetween(String guideId, TDate after,
			TDate before) throws DataSourceException;

}
