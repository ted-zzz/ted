package nu.ted.service;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import nu.ted.domain.Series;
import nu.ted.domain.SeriesDB;
import nu.ted.gen.SeriesSearchResult;
import nu.ted.gen.WatchedSeries;

import org.apache.thrift.TException;
import org.junit.Test;


public class TedTests
{

    private class TestSeriesDB implements SeriesDB
    {
        public List<Series> search(String Name) {
            List<Series> results = new LinkedList<Series>();
            if (Name.equalsIgnoreCase("Exactly")) {
                results.add(new Series("Exactly", "E"));
            } else if (Name.equalsIgnoreCase("General")) {
                results.add(new Series("General1", "1"));
                results.add(new Series("General2", "2"));
                results.add(new Series("General3", "3"));
            }
            return results;
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
    public void testNoWatchedByDefault() throws TException {
        TedServiceImpl ted = new TedServiceImpl(new TestSeriesDB());

        List<WatchedSeries> watched = ted.getWatched();
        assertNotNull(watched);
        assertEquals(0, watched.size());

    }

    @Test
    public void testWatchOneShow() throws TException {
        TedServiceImpl ted = new TedServiceImpl(new TestSeriesDB());

        boolean result = ted.watch("E");
        assertTrue(result);

        List<WatchedSeries> watched = ted.getWatched();
        assertEquals(1, watched.size());
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
