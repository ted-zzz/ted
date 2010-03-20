package nu.ted.guide;

import java.util.List;

import nu.ted.domain.Series;
import nu.ted.generated.ImageFile;
import nu.ted.generated.SeriesSearchResult;

public interface GuideDB
{
	List<SeriesSearchResult> search(String name);

	public ImageFile getBanner(String searchId);

	Series getSeriesFromUID(String searchId);
}
