package nu.ted.service;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import nu.ted.domain.SeriesBackendWrapper;
import nu.ted.event.EventFactory;
import nu.ted.event.EventRegistry;
import nu.ted.generated.Constants;
import nu.ted.generated.Event;
import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.InvalidOperation;
import nu.ted.generated.Series;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.generated.Ted;
import nu.ted.generated.TedService.Iface;
import nu.ted.guide.DataSourceException;
import nu.ted.guide.GuideDB;

import org.apache.thrift.TException;

public class TedServiceImpl implements Iface
{
	private static EventRegistry eventRegistry = EventRegistry.createEventRegistry(60000L, 30000L);

	private GuideDB seriesSource;
	private Ted ted;

	static short nextUID = 1; // TODO: static across restarts

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

	// TODO: throw more specific exception when DSE is fixed.
	@Override
	public short startWatching(String guideId) throws InvalidOperation, TException
	{
		Series s;
		try {
			Calendar startDate = Calendar.getInstance();
			startDate.add(Calendar.DAY_OF_YEAR, -14);
			s = seriesSource.getSeries(guideId, nextUID, startDate);
		} catch (DataSourceException e) {
			throw new TException(e);
		}

		synchronized (ted.getSeries()) {
			SeriesBackendWrapper sb = new SeriesBackendWrapper(s);
			for (Series existingSeries : ted.getSeries()) {
				if (sb.isSameSeries(existingSeries)) {
					throw new InvalidOperation("Series already exists on watched list and cannot be added again");
				}
			}

		}
		nextUID++;
		ted.getSeries().add(s);
		registerEvent(EventFactory.createWatchedSeriesAddedEvent(s));
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
		registerEvent(EventFactory.createWatchedSeriesRemovedEvent(watchMatch));
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

	public static void registerEvent(Event event) {
		eventRegistry.addEvent(event);
	}

}
