package nu.ted.service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.thrift.TException;


import nu.ted.domain.Series;
import nu.ted.domain.SeriesDB;
import nu.ted.gen.SeriesSearchResult;
import nu.ted.gen.WatchedSeries;
import nu.ted.gen.TedService.Iface;

public class TedServiceImpl implements Iface
{
	SeriesDB seriesSource;
	List<WatchedSeries> watched;
		
	// TODO: watched needs to be a singleton, or in another class
	public TedServiceImpl(SeriesDB seriesSource) {
		this.seriesSource = seriesSource;
		watched = new LinkedList<WatchedSeries>();
	}

	public List<SeriesSearchResult> search(String name) throws TException
	{
		List<SeriesSearchResult> results = new LinkedList<SeriesSearchResult>();
		for (Series series : seriesSource.search(name)) {
			SeriesSearchResult result = new SeriesSearchResult();
			
			// TODO: will need a factory for the gen <--> domain translations
			result.setUid(series.getUid());
			result.setName(series.getName());
			results.add(result);
			
		}
		return results;
	}

	public List<WatchedSeries> getWatched() throws TException
	{
		return Collections.unmodifiableList(watched);
	}

	public boolean watch(String uid) throws TException
	{
		// TODO: do UID->internalUID translation
		// TODO: handle NAME, Season, Episode
		// TODO: we'll probably keep track of something other then the UI version
		watched.add(new WatchedSeries(uid, "NAME", (short)1, (short)1));
		return true;
	}

}
