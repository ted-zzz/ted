package nu.ted.service;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.thrift.TException;

import nu.ted.generated.Constants;
import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.Series;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.generated.Ted;
import nu.ted.generated.TedService.Iface;
import nu.ted.guide.DataSourceException;
import nu.ted.guide.GuideDB;

public class TedServiceImpl implements Iface
{
	GuideDB seriesSource;
	private Ted ted;

	static short nextUID = 1; // TODO: static across restarts

	// TODO: watched needs to be a singleton, or in another class
	public TedServiceImpl(Ted ted, GuideDB seriesSource) {

		this.ted = ted;
		this.seriesSource = seriesSource;
	}

	@Override
	public int getVersion() throws TException {
		return Constants.PROTOCOL_VERSION;
	}

	@Override
	public List<SeriesSearchResult> search(String name) throws TException
	{
		try {
			return seriesSource.search(name);
		} catch (DataSourceException e) {
			// TODO: how does this work? Client won't know about cause classes? (more examples below)
			// TODO: need to define an exception in the thrift for these I think.
			throw new TException(e);
		}
	}

	@Override
	public List<Series> getWatching() throws TException
	{
		return Collections.unmodifiableList(ted.getSeries());
	}

	@Override
	public short startWatching(String guideId) throws TException
	{
		Series s;
		try {
			s = seriesSource.getSeries(guideId, nextUID, Calendar.getInstance());
		} catch (DataSourceException e) {
			throw new TException(e);
		}
		nextUID++;
		ted.getSeries().add(s);
		return s.getUid();
	}

	@Override
	public void stopWatching(short uid) throws TException
	{
		Series watchMatch = findWatched(uid);
		if (watchMatch == null || !ted.getSeries().remove(watchMatch)) {
			// TODO throw exception?
			return;
		}
	}

	@Override
	public ImageFile getImageByGuideId(String guideId, ImageType type)
		throws TException
	{
		try {
			return seriesSource.getImage(guideId, type);
		} catch (DataSourceException e) {
			throw new TException(e);
		}
	}

	@Override
	public ImageFile getImageBySeriesId(short uID, ImageType type)
		throws TException {
		Series series = findWatched(uID);
		if (series == null) {
			return new ImageFile();
		}

		try {
			return seriesSource.getImage(series.getGuideId(), type);
		} catch (DataSourceException e) {
			throw new TException(e);
		}
	}


	@Override
	public String getOverview(String searchUID) throws TException {
		try {
			return seriesSource.getOverview(searchUID);
		} catch (DataSourceException e) {
			throw new TException(e);
		}
	}

	private Series findWatched(short uID) {
		for (Series s : ted.getSeries()) {
			if (uID == s.getUid()) {
				return s;
			}
		}
		return null;
	}
}
