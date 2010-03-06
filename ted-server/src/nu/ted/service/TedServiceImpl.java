package nu.ted.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.thrift.TException;


import nu.ted.domain.Series;
import nu.ted.domain.SeriesDB;
import nu.ted.gen.SeriesSearchResult;
import nu.ted.gen.TedService.Iface;

public class TedServiceImpl implements Iface
{
	SeriesDB seriesSource;
		
	public TedServiceImpl(SeriesDB seriesSource) {
		this.seriesSource = seriesSource;
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

}
