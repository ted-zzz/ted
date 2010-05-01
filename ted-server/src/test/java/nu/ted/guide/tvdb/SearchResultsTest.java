package nu.ted.guide.tvdb;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import nu.ted.guide.tvdb.SearchResults.TVDBSeries;

import org.junit.Before;
import org.junit.Test;

public class SearchResultsTest
{
	private static class TestSearchResultsXML
	{
		private StringBuffer xml;

		public TestSearchResultsXML()
		{
			xml = new StringBuffer();
			xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			xml.append("<Data>");
		}

		public void addSeries(int id, String name)
		{
			xml.append("  <Series>\n");
			xml.append("    <seriesid>" + id + "</seriesid>");
			xml.append("    <SeriesName>" + name + "</SeriesName>");
			xml.append("    <FirstAired>" + "1/1/1" + "</FirstAired>");
			xml.append("    <Overview>" + "About Something" + "</Overview>");
			xml.append("  </Series>\n");
		}

		public String toString()
		{
			return xml.toString() + "</Data>";
		}

		public InputStream toStream() throws UnsupportedEncodingException
		{
			return new ByteArrayInputStream(this.toString().getBytes("UTF-8"));
		}

	}

	private TestSearchResultsXML xml;

	@Before
	public void setUp()
	{
		this.xml = new TestSearchResultsXML();
	}

	private void assertSeries(TVDBSeries tvdbSeries, String id, String name)
	{
		Assert.assertEquals(id, tvdbSeries.getId());
		Assert.assertEquals(name, tvdbSeries.getName());
	}

	@Test
	public void noResult() throws UnsupportedEncodingException
	{
		SearchResults results = SearchResults.create(xml.toStream());

		Assert.assertEquals(0, results.size());
	}

	@Test
	public void oneResult() throws UnsupportedEncodingException
	{
		this.xml.addSeries(1, "Result");

		SearchResults results = SearchResults.create(xml.toStream());

		Assert.assertEquals(1, results.size());
		assertSeries(results.getSeriesList().get(0), "1", "Result");
	}

	@Test
	public void multipleResult() throws UnsupportedEncodingException
	{
		this.xml.addSeries(1, "R1");
		this.xml.addSeries(2, "R2");

		SearchResults results = SearchResults.create(xml.toStream());

		Assert.assertEquals(2, results.size());
		assertSeries(results.getSeriesList().get(0), "1", "R1");
		assertSeries(results.getSeriesList().get(1), "2", "R2");

	}
}
