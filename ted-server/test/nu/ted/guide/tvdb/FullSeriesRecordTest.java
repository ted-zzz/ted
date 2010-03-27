package nu.ted.guide.tvdb;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Calendar;

import javax.xml.bind.DatatypeConverter;

import junit.framework.Assert;

import nu.ted.domain.Episode;

import org.junit.Before;
import org.junit.Test;

public class FullSeriesRecordTest
{
	private static class TestEpisodeListXML
	{
		private static final String EXPECTED_OVERVIEW = "This is the overview.";
		private StringBuffer xml;

		public TestEpisodeListXML()
		{
			xml = new StringBuffer();
			xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			xml.append("<Data>\n");
			xml.append("  <Series>\n");
			xml.append("    <id>1000</id>\n");
			xml.append("    <Actors>people</Actors>\n");
			xml.append("    <Overview>" + EXPECTED_OVERVIEW + "</Overview>\n");
			xml.append("  </Series>\n");
		}

		public void addEpisode(int season, int episode, String aired, String name)
		{
			xml.append("  <Episode>\n");
			xml.append("    <SeasonNumber>" + season + "</SeasonNumber>\n");
			xml.append("    <EpisodeNumber>" + episode + "</EpisodeNumber>\n");
			xml.append("    <FirstAired>" + aired + "</FirstAired>\n");
			xml.append("    <EpisodeName>" + name + "</EpisodeName>\n");
			xml.append("  </Episode>\n");
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

	private TestEpisodeListXML xml;

	@Before
	public void setUp()
	{
		this.xml = new TestEpisodeListXML();
	}

	private void assertEpisode(Episode episode, int season, int epnum, String aired, String name) throws ParseException
	{
		Assert.assertEquals(season, episode.getSeasonNum());
		Assert.assertEquals(epnum, episode.getEpisodeNum());
		Assert.assertEquals(DatatypeConverter.parseDate(aired), episode.getAired());
	}

	@Test
	public void shouldReturnNextEpsiodeFromOne() throws UnsupportedEncodingException, ParseException
	{
		Calendar now = Calendar.getInstance();
		Calendar then = (Calendar) now.clone();
		then.add(Calendar.DAY_OF_MONTH, 1);
		String date = DatatypeConverter.printDate(then);
		this.xml.addEpisode(1, 3, date, "Name");

		FullSeriesRecord list = FullSeriesRecord.create(xml.toStream());

		Episode result = list.getNextEpisode(now);
		Assert.assertNotNull(result);
		assertEpisode(result, 1, 3, date, "Name");
	}

	@Test
	public void shouldReturnNextFromMultiple() throws UnsupportedEncodingException, ParseException
	{
		Calendar now = Calendar.getInstance();

		Calendar before = ((Calendar) now.clone());
		before.add(Calendar.DAY_OF_MONTH, -1);

		Calendar after  = ((Calendar) now.clone());
		after.add(Calendar.DAY_OF_MONTH,  1);

		this.xml.addEpisode(1, 3, DatatypeConverter.printDate(before), "Name");
		this.xml.addEpisode(1, 4, DatatypeConverter.printDate(after), "Name2");

		FullSeriesRecord list = FullSeriesRecord.create(xml.toStream());

		Episode result = list.getNextEpisode(now);

		Assert.assertNotNull(result);
		assertEpisode(result, 1, 4, DatatypeConverter.printDate(after), "Name2");

	}

	@Test
	public void getOverviewFromXml() throws UnsupportedEncodingException
	{
		FullSeriesRecord series = FullSeriesRecord.create(xml.toStream());
		Assert.assertEquals(TestEpisodeListXML.EXPECTED_OVERVIEW, series.getOverview());
	}

	// TODO: decide if NextEpisode() on the date return this ep, or next

}
