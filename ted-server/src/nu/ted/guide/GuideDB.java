package nu.ted.guide;

import java.util.List;

import nu.ted.domain.Series;

public interface GuideDB
{
	List<Series> search(String name);
	
	Series getSeriesFromUID(String id);

}
