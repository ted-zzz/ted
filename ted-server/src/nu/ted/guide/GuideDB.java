package nu.ted.guide;

import java.util.List;

import nu.ted.domain.Series;
import nu.ted.gen.SeriesSearchResult;

public interface GuideDB
{
	List<SeriesSearchResult> search(String name);

	public byte[] getBanner(String searchId);

	Series getSeriesFromUID(String searchId);
}
