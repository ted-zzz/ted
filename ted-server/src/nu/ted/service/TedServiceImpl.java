package nu.ted.service;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.apache.thrift.TException;

import nu.ted.generated.Constants;
import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.Series;
import nu.ted.generated.SeriesSearchResult;
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
	public int getVersion() throws TException {
		return Constants.PROTOCOL_VERSION;
	}

	@Override
	public List<SeriesSearchResult> search(String name) throws TException
	{
		return seriesSource.search(name);
	}

	@Override
	public List<Series> getWatching() throws TException
	{
		return watched;
	}

	@Override
	public short startWatching(String guideId) throws TException
	{
		Series s = seriesSource.getSeries(guideId, nextUID, Calendar.getInstance());
		nextUID++;
		watched.add(s);
		return s.getUid();
	}

	@Override
	public void stopWatching(short uid) throws TException
	{
		Series watchMatch = findWatched(uid);
		if (watchMatch == null || !watched.remove(watchMatch)) {
			// TODO throw exception?
			return;
		}
	}

	@Override
	public ImageFile getImageByGuideId(String guideId, ImageType type)
		throws TException
	{
		return seriesSource.getImage(guideId, type);
	}

	@Override
	public ImageFile getImageBySeriesId(short uID, ImageType type)
		throws TException {
		Series series = findWatched(uID);
		if (series == null) {
			return new ImageFile();
		}

		return seriesSource.getImage(series.getGuideId(), type);
	}


	@Override
	public String getOverview(String searchUID) throws TException {
		return seriesSource.getOverview(searchUID);
	}

	private Series findWatched(short uID) {
		for (Series s : watched) {
			if (uID == s.getUid()) {
				return s;
			}
		}
		return null;
	}
}
