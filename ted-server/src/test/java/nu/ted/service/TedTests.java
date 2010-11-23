package nu.ted.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import nu.ted.Server;
import nu.ted.domain.SeriesBackendWrapper;
import nu.ted.generated.TDate;
import nu.ted.generated.Event;
import nu.ted.generated.EventType;
import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.InvalidOperation;
import nu.ted.generated.Series;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.generated.Ted;
import nu.ted.generated.TorrentSource;
import nu.ted.guide.TestGuide;

import org.apache.thrift.TException;
import org.junit.Test;

public class TedTests
{
	@Test
	public void testFindExactSeries() throws TException
	{
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());
		List<SeriesSearchResult> results = ted.search("Exactly");

		assertNotNull("Ted not returning a List", results);
		assertEquals("Only one show expected", 1, results.size());

		SeriesSearchResult exact = results.get(0);
		assertEquals("Name incorrect", "Exactly", exact.getName());
	}

	@Test
	public void testFindMultipleSeries() throws TException {
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());
		List<SeriesSearchResult>  results = ted.search("General");

		assertNotNull("Ted not returning a List", results);
		assertEquals("Expected multiple results", 3, results.size());

		SeriesSearchResult series = results.get(0);
		assertEquals("Name incorrect", "General1", series.getName());
		series = results.get(1);
		assertEquals("Name incorrect", "General2", series.getName());
		series = results.get(2);
		assertEquals("Name incorrect", "General3", series.getName());

	}

	@Test
	public void shouldStartOutEmpty() throws TException {
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());

		List<Series> watched = ted.getWatching();
		assertNotNull(watched);
		assertEquals(0, watched.size());
	}

	@Test
	public void shouldBeAbleToWatchOneValidShow() throws TException, InvalidOperation {
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());

		ted.startWatching("E");

		List<Series> watched = ted.getWatching();
		assertEquals(1, watched.size());

		Series series = watched.get(0);
		assertEquals("Exactly", series.getName());

		assertNull(new SeriesBackendWrapper(series).getLastEpisode());

	}

	@Test
	public void shouldIncrementSeriesUidCacheNextIdAfterSuccessfulWatch()
		throws TException, InvalidOperation {
		Ted defaultTed = Server.createDefaultTed();
		TedServiceImpl tedService = new TedServiceImpl(defaultTed, new TestGuide());
		assertEquals(1, defaultTed.getSeriesUidCache().getNextUid());
		tedService.startWatching("E");
		assertEquals(2, defaultTed.getSeriesUidCache().getNextUid());
		List<Series> watched = tedService.getWatching();
		assertEquals(1, watched.size());
	}

	@Test
	public void shouldNotIncrementSeriesUidCacheNextIdAfterUnsuccessfulWatch()
		throws TException, InvalidOperation {
		Ted defaultTed = Server.createDefaultTed();
		TedServiceImpl tedService = new TedServiceImpl(defaultTed, new TestGuide());
		assertEquals(1, defaultTed.getSeriesUidCache().getNextUid());
		tedService.startWatching("E");
		assertEquals(2, defaultTed.getSeriesUidCache().getNextUid());
		try {
			tedService.startWatching("E");
			fail("Should have thrown exception. Duplicate.");
		}
		catch (InvalidOperation e) {
			// expected.
		}
		assertEquals(2, defaultTed.getSeriesUidCache().getNextUid());
		List<Series> watched = tedService.getWatching();
		assertEquals(1, watched.size());
	}

	@Test
	public void shouldBeAbleToStopWatchingAShowYourWatching() throws TException, InvalidOperation {
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());
		short id = ted.startWatching("E");

		ted.stopWatching(id);

		List<Series> watched = ted.getWatching();
		assertEquals(0, watched.size());
	}

	@Test
	public void shouldLoadBannerForValidSearchId() throws TException {
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());

		ImageFile image = ted.getImageByGuideId("E", ImageType.BANNER);
		assertNotNull(image);
		assertEquals("image/banner", image.getMimetype());
		assertArrayEquals("BANNER".getBytes(), image.getData());
	}

	@Test
	public void shouldLoadBannerThumbnailForValidSearchId() throws TException {
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());

		ImageFile image = ted.getImageByGuideId("E", ImageType.BANNER_THUMBNAIL);
		assertNotNull(image);
		assertEquals("image/thumbnail", image.getMimetype());
		assertArrayEquals("THUMBNAIL".getBytes(), image.getData());
	}

	@Test
	public void shouldLoadBannerForValidSeriesId() throws TException, InvalidOperation {
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());
		short id = ted.startWatching("E");

		ImageFile image = ted.getImageBySeriesId(id, ImageType.BANNER);
		assertNotNull(image);
		assertEquals("image/banner", image.getMimetype());
		assertArrayEquals("BANNER".getBytes(), image.getData());
	}

	@Test
	public void shouldLoadBannerThumbnailForValidSeriesId() throws TException, InvalidOperation {
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());
		short id = ted.startWatching("E");

		ImageFile image = ted.getImageBySeriesId(id, ImageType.BANNER_THUMBNAIL);
		assertNotNull(image);
		assertEquals("image/thumbnail", image.getMimetype());
		assertArrayEquals("THUMBNAIL".getBytes(), image.getData());
	}

	@Test
	public void ensureGetOverviewFromService() throws TException
	{
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());
		assertEquals("An Overview", ted.getOverview("E"));
	}

	@Test
	public void shouldRegisterWatchedListChangedEventIfStartWatching() throws TException, InvalidOperation{
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, -1);
		TDate lastCheck = new TDate(calendar.getTimeInMillis());

		TedServiceImpl.clearRegistryEvents();

		ted.startWatching("E");
		List<Event> events = ted.getEvents(lastCheck);
		assertEquals(1, events.size());
		assertEquals(EventType.WATCHED_SERIES_ADDED, events.get(0).getType());
	}

	@Test(expected = InvalidOperation.class)
	public void shouldOnlyAllowShowsToBeAddedOnce() throws TException, InvalidOperation{
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());

		ted.startWatching("E");
		ted.startWatching("E");
		fail("Shouldn't be able to add show twice.");
	}

	@Test
	public void shouldRegisterWatchedListChangedEventIfStopWatching() throws TException, InvalidOperation, InterruptedException {
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());
		short id = ted.startWatching("E");

		TedServiceImpl.clearRegistryEvents();

		ted.stopWatching(id);

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -1);
		TDate lastCheck = new TDate(calendar.getTimeInMillis());
		List<Event> events = ted.getEvents(lastCheck);

		assertEquals(1, events.size());
		assertEquals(EventType.WATCHED_SERIES_REMOVED, events.get(0).getType());
	}

	@Test
	public void ensureGetSeriesReturnsWatchedSeries() throws Exception {
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());
		short seriesId = ted.startWatching("E");
		Series series = ted.getSeries(seriesId);
		assertNotNull("Series was not found.", series);
		assertEquals(seriesId, series.getUid());
	}

	@Test
	public void ensureNullWhenGetSeriesDoesNotFindSeriesWithSpecifiedId() throws Exception {
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());
		Series series = ted.getSeries(new Short("123"));
		assertNull("Series should not have been found.", series);
	}

	// This test might not make sense if we have a default list created.
	@Test
	public void ensureNoTorrentSourcesExistByDefault() throws Exception {
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());

		List<TorrentSource> allTorrentSources = ted.getTorrentSources();
		assertNotNull(allTorrentSources);
		assertEquals("Should have no sources by default", 0, allTorrentSources.size());
	}

	@Test
	public void ensureReplacingTorrentSourceWorks() throws Exception {
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());

		TorrentSource source1 = new TorrentSource((short) 1, "type", "name1", "location1");
		TorrentSource source2 = new TorrentSource((short) 2, "type", "name2", "location2");
		TorrentSource source3 = new TorrentSource((short) 3, "type", "name3", "location3");

		List<TorrentSource> torrentSourceList = new LinkedList<TorrentSource>();
		torrentSourceList.add(source1);

		ted.updateTorrentSources(torrentSourceList);

		List<TorrentSource> sources = ted.getTorrentSources();
		assertNotNull(sources);
		assertEquals("Should have the one I entered", 1, sources.size());
		assertEquals("And it's values should be correct", "location1", sources.get(0).getLocation());

		torrentSourceList = new LinkedList<TorrentSource>();
		torrentSourceList.add(source2);
		torrentSourceList.add(source3);

		ted.updateTorrentSources(torrentSourceList);

		sources = ted.getTorrentSources();
		assertNotNull(sources);
		assertEquals("Should have the two new ones", 2, sources.size());
		assertFalse("location1 shouldn't be included", sources.get(0).getLocation().equals("location1"));
		assertFalse("location1 shouldn't be included", sources.get(1).getLocation().equals("location1"));
	}

	@Test
	public void ensureNullTorrentListIsError() throws Exception {
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());
		ted.updateTorrentSources(null);

		List<TorrentSource> sources = ted.getTorrentSources();
		assertNotNull(sources);
		assertEquals("Should have 0 elements", 0, sources.size());
	}

	@Test
	public void getTorrentSource() throws InvalidOperation {
		TorrentSource source = new TorrentSource((short) 0, "RSS", "SRC", "LOC");

		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());
		ted.addTorrentSource(source);

		TorrentSource fetched = ted.getTorrentSource(source.getUid());
		assertEquals(source, fetched);
	}

	@Test(expected=InvalidOperation.class)
	public void ensureGetTorrentSourceThrowsExceptionIfTorrentSourceNotFound()
		throws InvalidOperation {
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());
		ted.getTorrentSource((short) 1);
	}

	@Test
	public void addTorrentSource() throws InvalidOperation, TException {
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());

		TorrentSource source = new TorrentSource((short) 0, "RSS", "NEW_SOURCE", "LOC");
		ted.addTorrentSource(source);

		List<TorrentSource> fetched = ted.getTorrentSources();
		assertEquals(1, fetched.size());
		assertTrue(fetched.contains(source));
	}

	@Test
	public void ensureAddTorrentSourceUpdatesSourceUidCacheNextId()
		throws InvalidOperation, TException {
		Ted ted = Server.createDefaultTed();
		TedServiceImpl tedService = new TedServiceImpl(ted, new TestGuide());
		assertEquals(1, ted.getSourceUidCache().getNextUid());
		TorrentSource source = new TorrentSource((short) 0, "RSS", "NEW_SOURCE", "LOC");
		tedService.addTorrentSource(source);
		assertEquals(2, ted.getSourceUidCache().getNextUid());
	}

	@Test
	public void addTorrentSourceThrowsExceptionIfSourceExistsWithSameName()
		throws InvalidOperation, TException {
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());

		String expectedName = "TEST_SOURCE";
		TorrentSource existingSource = new TorrentSource((short) 0, "RSS", expectedName, "LOC");
		ted.addTorrentSource(existingSource);

		try {
			// Add the source again - expect exception.
			ted.addTorrentSource(existingSource);
			fail("Should have thrown exception since source already existed.");
		}
		catch (InvalidOperation e) {
			String expectedException = "Could not add torrent source. A source " +
				"with the name " + existingSource.getName() + " already exists.";
			assertEquals(expectedException, e.getMessage());
		}
	}

	@Test
	public void removeTorrentSource() throws InvalidOperation, TException {
		TorrentSource torrentSource = new TorrentSource((short) 0, "RSS",
				"TO_REMOVE", "LOC");

		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());
		ted.addTorrentSource(torrentSource);

		ted.removeTorrentSource(torrentSource.getUid());

		List<TorrentSource> fetched = ted.getTorrentSources();
		assertTrue(fetched.isEmpty());
	}

	@Test
	public void removeTorrentSourceThrowsExceptionIfSourceDoesNotExist()
		throws InvalidOperation, TException {
		short expectedId = (short) 1;
		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());
		try {
			ted.removeTorrentSource(expectedId);
			fail("Should have thrown exception since source does not exist.");
		}
		catch (InvalidOperation e) {
			String expectedException = "Could not remove torrent source: " +
					expectedId + ". Source does not exist.";
			assertEquals(expectedException, e.getMessage());
		}
	}

	@Test
	public void updateTorrentSource() throws InvalidOperation, TException {
		TorrentSource originalSource = new TorrentSource((short) 0, "RSS", "TEST_SOURCE", "LOC");

		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());
		ted.addTorrentSource(originalSource);

		TorrentSource containsUpdates = new TorrentSource(originalSource.getUid(),
				"UPDATED_TYPE", "UPDATED_SOURCE", "UPDATED_LOC");

		ted.updateTorrentSource(containsUpdates);

		List<TorrentSource> fetched = ted.getTorrentSources();
		assertEquals(1, fetched.size());
		TorrentSource updated = fetched.get(0);
		assertEquals(containsUpdates.getType(), updated.getType());
		assertEquals(containsUpdates.getName(), updated.getName());
		assertEquals(containsUpdates.getLocation(), updated.getLocation());
	}

	@Test
	public void updateTorrentSourceThrowsExceptionIfSourceWithOriginalNameExists()
		throws InvalidOperation, TException {
		String originalName = "TEST_SOURCE";

		TorrentSource existingSource1 = new TorrentSource((short) 0, "RSS",
				originalName, "LOC");
		TorrentSource existingSource2 = new TorrentSource((short) 0, "RSS",
				originalName + "2", "LOC");

		TedServiceImpl ted = new TedServiceImpl(Server.createDefaultTed(), new TestGuide());
		ted.addTorrentSource(existingSource1);
		ted.addTorrentSource(existingSource2);

		TorrentSource updated = new TorrentSource(existingSource2.getUid(),
				"RSS", originalName, "LOC");
		try {
			ted.updateTorrentSource(updated);
			fail("Should have thrown exception since original name did not exist.");
		}
		catch (InvalidOperation e) {
			String expectedException = "Could not update torrent source: " +
				updated.getUid() + ". A source with the name " +
				updated.getName() + " already exists.";

			assertEquals(expectedException, e.getMessage());
		}
	}

}
