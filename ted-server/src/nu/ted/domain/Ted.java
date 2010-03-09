package nu.ted.domain;

import java.util.LinkedList;
import java.util.List;

import nu.ted.guide.GuideDB;

// Class for top of the API
public class Ted
{
	GuideDB seriesSource;
	
	public Ted(GuideDB seriesSource) {
		this.seriesSource = seriesSource;
	}
	
	public List<Series> findSeries(String name) {
		return seriesSource.search(name);
	}
	
	public List<Episode> listEpisodes(Series series) {
		return new LinkedList<Episode>();
	}

}
