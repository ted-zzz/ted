package nu.ted.service;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import nu.ted.domain.SeriesBackendWrapper;
import nu.ted.generated.Episode;
import nu.ted.generated.ImageFile;
import nu.ted.generated.Series;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.guide.TestGuide;

import org.apache.thrift.TException;
import org.junit.Test;


public class TedTests
{
	@Test
	public void testFindExactSeries() throws TException
	{
		TedServiceImpl ted = new TedServiceImpl(new TestGuide());
		List<SeriesSearchResult> results = ted.search("Exactly");

		assertNotNull("Ted not returning a List", results);
		assertEquals("Only one show expected", 1, results.size());

		SeriesSearchResult exact = results.get(0);
		assertEquals("Name incorrect", "Exactly", exact.getName());
	}

	@Test
	public void testFindMultipleSeries() throws TException {
		TedServiceImpl ted = new TedServiceImpl(new TestGuide());
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
		TedServiceImpl ted = new TedServiceImpl(new TestGuide());

		List<Series> watched = ted.getWatching();
		assertNotNull(watched);
		assertEquals(0, watched.size());

	}

	@Test
	public void shouldBeAbleToWatchOneValidShow() throws TException {
		TedServiceImpl ted = new TedServiceImpl(new TestGuide());

		ted.startWatching("E");

		List<Series> watched = ted.getWatching();
		assertEquals(1, watched.size());

		Series series = watched.get(0);
		assertEquals("Exactly", series.getName());

		Episode currentEpisode = (new SeriesBackendWrapper(series)).getLastEpisode();
		assertNotNull(currentEpisode);
		assertEquals(4, currentEpisode.getSeason());
		assertEquals(2, currentEpisode.getNumber());

		Calendar oneDayAgo = Calendar.getInstance();
		oneDayAgo.add(Calendar.DAY_OF_MONTH, -2);
		
		Calendar aired = Calendar.getInstance();
		// TODO: should we zero mills in the cal creations
		aired.setTimeInMillis(currentEpisode.getAired());
		aired.set(Calendar.MILLISECOND, 0);
		oneDayAgo.set(Calendar.MILLISECOND, 0);
		assertEquals(oneDayAgo, aired);
		// NB: returned UID may not match Search UID
	}


	@Test
	public void shouldBeAbleToStopWatchingAShowYourWatching() throws TException {
		TedServiceImpl ted = new TedServiceImpl(new TestGuide());
		short id = ted.startWatching("E");

		ted.stopWatching(id);

		List<Series> watched = ted.getWatching();
		assertEquals(0, watched.size());
	}

	@Test
	public void shouldLoadBannerForValidShow() throws TException {
		TedServiceImpl ted = new TedServiceImpl(new TestGuide());

		ImageFile image = ted.getBanner("E");
		assertNotNull(image);
		assertEquals("image/cool", image.getMimetype());
		assertArrayEquals("ABCD".getBytes(), image.getData());
	}

	@Test
	public void ensureGetOverviewFromService() throws TException
	{
		TedServiceImpl ted = new TedServiceImpl(new TestGuide());
		assertEquals("An Overview", ted.getOverview("E"));
	}
}
