package nu.ted.guide.tvdb;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Calendar;

import javax.xml.bind.DatatypeConverter;

import junit.framework.Assert;
import nu.ted.domain.TestSeriesXml;
import nu.ted.generated.Episode;

import org.junit.Before;
import org.junit.Test;

public class FullSeriesRecordTest
{
	private TestSeriesXml xml;

	@Before
	public void setUp()
	{
		this.xml = new TestSeriesXml();
	}

	private void assertEpisode(Episode episode, short season, short epnum, Calendar aired, String name) throws ParseException
	{
		Assert.assertEquals(season, episode.getSeason());
		Assert.assertEquals(epnum, episode.getNumber());

		aired.set(Calendar.MILLISECOND, 0);

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(episode.getAired());
		cal.set(Calendar.MILLISECOND, 0);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(episode.getAired());
		cal2.set(Calendar.MILLISECOND, 0);
		System.out.println("Cal2 is " + cal2.toString());

		Assert.assertEquals(aired.getTimeInMillis(), episode.getAired());
	}

	private void zeroTimeOnCal(Calendar cal) {
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
	}

	@Test
	public void shouldReturnNextEpsiodeFromOne() throws UnsupportedEncodingException, ParseException
	{
		Calendar now = Calendar.getInstance();
		zeroTimeOnCal(now);

		Calendar then = (Calendar) now.clone();
		then.add(Calendar.DAY_OF_MONTH, 1);
		String date = DatatypeConverter.printDate(then);
		this.xml.addEpisode(1, 3, date, "Name");

		FullSeriesRecord list = FullSeriesRecord.create(xml.toStream());

		Episode result = list.getNextEpisode(now);
		Assert.assertNotNull(result);
		assertEpisode(result, (short) 1, (short) 3, then, "Name");
	}

	@Test
	public void shouldReturnNextFromMultiple() throws UnsupportedEncodingException, ParseException
	{
		Calendar now = Calendar.getInstance();
		zeroTimeOnCal(now);

		Calendar before = ((Calendar) now.clone());
		before.add(Calendar.DAY_OF_MONTH, -1);

		Calendar after  = ((Calendar) now.clone());
		after.add(Calendar.DAY_OF_MONTH,  1);

		this.xml.addEpisode(1, 3, DatatypeConverter.printDate(before), "Name");
		this.xml.addEpisode(1, 4, DatatypeConverter.printDate(after), "Name2");

		FullSeriesRecord list = FullSeriesRecord.create(xml.toStream());

		Episode result = list.getNextEpisode(now);

		Assert.assertNotNull(result);
		assertEpisode(result, (short) 1, (short) 4, after, "Name2");

	}

	@Test
	public void getOverviewFromXml() throws UnsupportedEncodingException
	{
		FullSeriesRecord series = FullSeriesRecord.create(xml.toStream());
		Assert.assertEquals(TestSeriesXml.EXPECTED_OVERVIEW, series.getOverview());
	}

	// TODO: decide if NextEpisode() on the date return this ep, or next

}
