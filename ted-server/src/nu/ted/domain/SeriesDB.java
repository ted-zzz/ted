package nu.ted.domain;

import java.util.List;

public interface SeriesDB
{
	List<Series> search(String name);
	
	Series getSeriesFromUID(String id);

}
