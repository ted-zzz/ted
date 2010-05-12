package nu.ted.service;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.thrift.TException;

import nu.ted.event.EventRegistry;
import nu.ted.generated.Constants;
import nu.ted.generated.Event;
import nu.ted.generated.EventType;
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
	// TODO [MS] This is not the right place for this. Currently the Ted
	//           class is generated, however it feels like it should be
	//           the main application class for Ted. The registry belongs
	//           with it.
	private EventRegistry eventRegistry;

	static short nextUID = 1; // TODO: static across restarts

	public TedServiceImpl(Ted ted, GuideDB seriesSource) {
		this.ted = ted;
		this.seriesSource = seriesSource;
		this.eventRegistry = EventRegistry.createEventRegistry(60000L, 30000L);
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
			Calendar startDate = Calendar.getInstance();
			startDate.add(Calendar.DAY_OF_YEAR, -14);
			s = seriesSource.getSeries(guideId, nextUID, startDate);
		} catch (DataSourceException e) {
			throw new TException(e);
		}
		nextUID++;
		ted.getSeries().add(s);
		eventRegistry.addEvent(new Event(EventType.WATCHED_SERIES_ADDED, s));
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
		eventRegistry.addEvent(new Event(EventType.WATCHED_SERIES_REMOVED, watchMatch));
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

	@Override
	public String registerClientWithEventRegistry() throws TException {
		return eventRegistry.registerClient();
	}

	@Override
	public List<Event> getEvents(String eventRegistryClientId)
			throws TException {
		return eventRegistry.getEvents(eventRegistryClientId);
	}

	@Override
	public Series getSeries(short uID) throws TException {
		return findWatched(uID);
	}

	/**
	 * Wipe all client specific data in logout. The thread may be reused.
	 */
	@Override
	public void logout() throws TException {
	}

}
