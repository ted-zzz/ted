package nu.ted.domain;

import java.util.LinkedList;
import java.util.List;

// Class for top of the API
public class Ted
{
	SeriesDB seriesSource;
	
	public Ted(SeriesDB seriesSource) {
		this.seriesSource = seriesSource;
	}
	
	public List<Series> findSeries(String name) {
		return seriesSource.search(name);
	}
	
	public List<Episode> listEpisodes(Series series) {
		return new LinkedList<Episode>();
	}

}
