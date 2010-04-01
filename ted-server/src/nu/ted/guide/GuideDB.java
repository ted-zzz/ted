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

	List<SeriesSearchResult> search(String name);

	Series getSeries(String guideId, short id, Calendar date);

	String getName(String guideId);

	List<Episode> getNewAiredEpisodes(String guideId, Calendar date, Episode lastEpisode);

	String getOverview(String guideId);

	ImageFile getImage(String guideId, ImageType type);

}
