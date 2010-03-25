package nu.ted.service;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import nu.ted.domain.Episode;
import nu.ted.generated.CurrentEpisode;
import nu.ted.generated.ImageFile;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.generated.WatchedSeries;
import nu.ted.guide.GuideDB;

import org.apache.thrift.TException;
import org.junit.Test;


public class TedTests
{

	private class TestSeriesDB implements GuideDB
	{
		public List<SeriesSearchResult> search(String name) {
			List<SeriesSearchResult> results = new LinkedList<SeriesSearchResult>();
			if (name.equalsIgnoreCase("Exactly")) {
				results.add(new SeriesSearchResult("E", "Exactly"));
			} else if (name.equalsIgnoreCase("General")) {
				results.add(new SeriesSearchResult("1", "General1"));
				results.add(new SeriesSearchResult("2", "General2"));
				results.add(new SeriesSearchResult("3", "General3"));
			}
			return results;
		}

		public ImageFile getBanner(String id) {
			if (id.equals("E")) {
				return new ImageFile("image/cool", "ABCD".getBytes());
			}
			return new ImageFile();
		}

		public String getName() {
			return "TEST GUIDE";
		}

		public String getName(String guideId) {
			if (guideId.equals("E")) {
				return "Exactly";
			}
			return null;
		}

		public Episode getLastEpisode(String guideId, Calendar date) {
			Calendar oneDayAgo = (Calendar) date.clone();
			oneDayAgo.add(Calendar.DAY_OF_MONTH, -1);
			if (guideId.equals("E")) {
				return new Episode(4, 2, oneDayAgo);
			}
			return null;
		}

		@Override
		public String getOverview(String guideId) {
			return "An Overview";
		}
	}

	// TODO: Don't like TException bleeding into other code, can if be avoided?

	@Test
	public void testFindExactSeries() throws TException
	{
		TedServiceImpl ted = new TedServiceImpl(new TestSeriesDB());
		List<SeriesSearchResult> results = ted.search("Exactly");

		assertNotNull("Ted not returning a List", results);
		assertEquals("Only one show expected", 1, results.size());

		SeriesSearchResult exact = results.get(0);
		assertEquals("Name incorrect", "Exactly", exact.getName());
	}

	@Test
	public void testFindMultipleSeries() throws TException {
		TedServiceImpl ted = new TedServiceImpl(new TestSeriesDB());
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
		TedServiceImpl ted = new TedServiceImpl(new TestSeriesDB());

		List<WatchedSeries> watched = ted.getWatching();
		assertNotNull(watched);
		assertEquals(0, watched.size());

	}

	@Test
	public void shouldBeAbleToWatchOneValidShow() throws TException {
		TedServiceImpl ted = new TedServiceImpl(new TestSeriesDB());

		ted.startWatching("E");

		List<WatchedSeries> watched = ted.getWatching();
		assertEquals(1, watched.size());

		WatchedSeries series = watched.get(0);
		assertEquals("Exactly", series.getName());

		CurrentEpisode currentEpisode = series.getCurrentEpisde();
		assertNotNull(currentEpisode);
		assertEquals(4, currentEpisode.getSeason());
		assertEquals(2, currentEpisode.getNumber());

		Calendar oneDayAgo = Calendar.getInstance();
		oneDayAgo.add(Calendar.DAY_OF_MONTH, -1);
		String dateString = DatatypeConverter.printDate(oneDayAgo);
		assertEquals(dateString, currentEpisode.getAired());
		// NB: returned UID may not match Search UID
	}


	@Test
	public void shouldBeAbleToStopWatchingAShowYourWatching() throws TException {
		TedServiceImpl ted = new TedServiceImpl(new TestSeriesDB());
		short id = ted.startWatching("E");

		ted.stopWatching(id);

		List<WatchedSeries> watched = ted.getWatching();
		assertEquals(0, watched.size());
	}

	@Test
	public void shouldLoadBannerForValidShow() throws TException {
		TedServiceImpl ted = new TedServiceImpl(new TestSeriesDB());

		ImageFile image = ted.getBanner("E");
		assertNotNull(image);
		assertEquals("image/cool", image.getMimetype());
		assertArrayEquals("ABCD".getBytes(), image.getData());
	}

	@Test
	public void shouldReturnNextShowsItsWaitingOn() throws TException {
		TedServiceImpl ted = new TedServiceImpl(new TestSeriesDB());

		// List<Episode> episodes = ted.getWaitingEpisodes();

	}

	// Not yet @Test
//	public void testMoreInfoAboutSeries()
//	{
//		TedServiceImpl ted = new TedServiceImpl(new TestSeriesDB());
//		List<Series> results = ted.search("Exactly");
//		Series series = results.get(0);
//
//		List<Episode> episodes = ted.listEpisodes(series);
//		assertNotNull("Ted unable to find episodes", episodes);
//		assertEquals("Ted didn't find episodes", 2, episodes.size());
//
//	}

}
