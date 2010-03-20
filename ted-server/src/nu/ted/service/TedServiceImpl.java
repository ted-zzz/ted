package nu.ted.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.thrift.TException;

import nu.ted.domain.Series;
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

	public List<SeriesSearchResult> search(String name) throws TException
	{
		return seriesSource.search(name);
	}

	public List<WatchedSeries> getWatching() throws TException
	{
		List<WatchedSeries> results = new LinkedList<WatchedSeries>();
		for (Series s : watched) {
			// TODO: gen <--> domain translation
			// TODO: season/episode wrong
			// TODO: UID may be wrong is translating to SearchSource
			WatchedSeries ws = new WatchedSeries(s.getUid(), s.getName(), (short)1, (short)1);
			results.add(ws);
		}
		return results;
	}

	public short startWatching(String searchUID) throws TException
	{
		// TODO: do UID->internalUID translation
		// TODO: handle NAME, Season, Episode
		Series s = seriesSource.getSeriesFromUID(searchUID);
		s.setUID(nextUID);
		nextUID++;
		// TODO: check null UID
		// TODO: check null result
		watched.add(s);
		return s.getUid();
	}
	
	public void stopWatching(short uid) throws TException
	{
		for (Series s : watched) {
			if (s.getUid() == uid) {
				if (!watched.remove(s)) {
					// TODO: throw exception;
				}
				return;
			}
			
		}
		// TODO: throw exception
	}

	public byte[] getBanner(String searchUID) throws TException
	{
		return seriesSource.getBanner(searchUID);
	}
}
