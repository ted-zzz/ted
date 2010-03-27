package nu.ted.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.thrift.TException;

import nu.ted.domain.Series;
import nu.ted.generated.ImageFile;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.generated.WatchedSeries;
import nu.ted.generated.TedService.Iface;
import nu.ted.guide.GuideDB;

public class TedServiceImpl implements Iface
{
	GuideDB seriesSource;
	List<Series> watched;
	
	static short nextUID = 1; // TODO: static across restarts
	
	// TODO: watched needs to be a singleton, or in another class
	public TedServiceImpl(GuideDB seriesSource) {
		this.seriesSource = seriesSource;
		watched = new LinkedList<Series>();
	}

	@Override
	public List<SeriesSearchResult> search(String name) throws TException
	{
		return seriesSource.search(name);
	}

	@Override
	public List<WatchedSeries> getWatching() throws TException
	{
		List<WatchedSeries> results = new LinkedList<WatchedSeries>();
		for (Series s : watched) {
			results.add(s.getWatchedSeries());
		}
		return results;
	}

	@Override
	public short startWatching(String searchUID) throws TException
	{
		// TODO: handle NAME, Season, Episode
		Series s = new Series(nextUID, seriesSource, searchUID);
		nextUID++;
		// TODO: check null UID
		// TODO: check null result
		watched.add(s);
		return s.getUID();
	}
	
	@Override
	public void stopWatching(short uid) throws TException
	{
		for (Series s : watched) {
			if (s.getUID() == uid) {
				if (!watched.remove(s)) {
					// TODO: throw exception;
				}
				return;
			}
			
		}
		// TODO: throw exception
	}

	@Override
	public ImageFile getBanner(String searchUID) throws TException
	{
		return seriesSource.getBanner(searchUID);
	}

	@Override
	public String getOverview(String searchUID) throws TException {
		return seriesSource.getOverview(searchUID);
	}
}
