package nu.ted.guide;

import java.util.Calendar;
import java.util.List;

import nu.ted.DataRetrievalException;
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

	List<SeriesSearchResult> search(String name) throws DataRetrievalException;

	Series getSeries(String guideId, short id, Calendar date) throws DataRetrievalException;

	String getName(String guideId) throws DataRetrievalException;

	String getOverview(String guideId) throws DataRetrievalException;

	ImageFile getImage(String guideId, ImageType type) throws DataRetrievalException;

	List<Episode> getAiredEpisodesBetween(String guideId, TDate after,
			TDate before) throws DataRetrievalException;

}
