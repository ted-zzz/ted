package nu.ted.guide;

import java.util.Calendar;
import java.util.List;

import nu.ted.domain.Episode;
import nu.ted.generated.ImageFile;
import nu.ted.generated.SeriesSearchResult;

public interface GuideDB
{
	// Get the name for this guide.
	String getName();

	List<SeriesSearchResult> search(String name);

	ImageFile getBanner(String guideId);

	String getName(String guideId);

	Episode getLastEpisode(String guideId, Calendar date);
}
