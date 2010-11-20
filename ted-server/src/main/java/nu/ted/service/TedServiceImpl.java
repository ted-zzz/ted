package nu.ted.service;

import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.thrift.TException;

import nu.ted.client.Client;
import nu.ted.client.ClientIdGenerator;
import nu.ted.domain.SeriesBackendWrapper;
import nu.ted.event.EventFactory;
import nu.ted.event.EventRegistry;
import nu.ted.generated.Constants;
import nu.ted.generated.TDate;
import nu.ted.generated.Event;
import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.InvalidOperation;
import nu.ted.generated.Series;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.generated.Ted;
import nu.ted.generated.TedService.Iface;
import nu.ted.generated.TorrentSource;
import nu.ted.guide.DataSourceException;
import nu.ted.guide.GuideDB;

public class TedServiceImpl implements Iface
{
	private static EventRegistry eventRegistry = EventRegistry.createEventRegistry(60000L, 1);

	private GuideDB seriesSource;
	private Ted ted;

	private static ClientIdGenerator idGenerator = new ClientIdGenerator();
	private static final class ClientHolder {
		// Not using an initialValue here because the clientHolder will be
		// cleaned after every connection, but the thread will remain.
		// initialValue would only be called the first time.
		private static ThreadLocal<Client> clientHolder = new ThreadLocal<Client>();

		public static Client getClient() {
			Client client = clientHolder.get();
			if (client == null) {
				client = new Client(idGenerator.generateClientId());
			}
			clientHolder.set(client);
			return client;
		}

		public static void clearClient() {
			clientHolder.set(null);
		}
	}

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
		short nextUid = ted.getSeriesUidCache().getNextUid();
		Series s;
		try {
			Calendar startDate = Calendar.getInstance();
			startDate.add(Calendar.DAY_OF_YEAR, -14);
			s = seriesSource.getSeries(guideId, nextUid, startDate);
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

		ted.getSeriesUidCache().setNextUid(++nextUid);
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
	public List<Event> getEvents(TDate date)
			throws TException {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getValue());
		return eventRegistry.getEvents(cal.getTime());
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
		ClientHolder.clearClient();
	}

	public static void registerEvent(Event event) {
		eventRegistry.addEvent(event);
	}

	@Override
	public List<TorrentSource> getTorrentSources() throws TException {
		return Collections.unmodifiableList(ted.getConfig().getTorrentSources());
	}

	@Override
	public void updateTorrentSources(List<TorrentSource> sources)
		throws TException {

		// Was going to make this catachable, but it's a coding error, so make it a Runtime exception.
		if (sources == null) {
			sources = new LinkedList<TorrentSource>();
		}
		// TODO: notify anyone? Clone map?
		ted.getConfig().setTorrentSources(sources);
	}

}
