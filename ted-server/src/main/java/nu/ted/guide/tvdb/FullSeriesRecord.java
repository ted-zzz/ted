package nu.ted.guide.tvdb;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import nu.ted.generated.Date;
import nu.ted.generated.Episode;

@XmlRootElement(name = "Data")
public class FullSeriesRecord {

	public static class TVDBSeries {

		@XmlElement(name = "SeriesName")
		private String name;

		@XmlElement(name = "banner")
		private String banner;

		@XmlElement(name = "Overview")
		private String overview;
	}

	public static class TVDBEpisode
	{
		public TVDBEpisode(int season, int number, Calendar aired, String name) {
			this.season = season;
			this.number = number;
			this.firstAired = (Calendar) aired.clone();
			this.title = name;
		}

		@SuppressWarnings("unused")
		private TVDBEpisode() { // For JAXB
			return;
		}

		public boolean equalsEpisode(Episode episode) {
			if (season != episode.getSeason())
				return false;

			if (number != episode.getNumber())
				return false;

			return true;
		}

		@XmlElement(name = "SeasonNumber")
		private int season;

		@XmlElement(name = "EpisodeNumber")
		private int number;

		@XmlElement(name = "FirstAired")
		private Calendar firstAired;

		@XmlElement(name = "EpisodeName")
		private String title;

		public int getSeason()
		{
			return season;
		}

		public int getEpisodeNumber()
		{
			return number;
		}

		private void zeroTimeOnFirstAired() {
			if (firstAired == null) {
				return;
			}

			firstAired.set(Calendar.HOUR, 0);
			firstAired.set(Calendar.MINUTE, 0);
			firstAired.set(Calendar.SECOND, 0);
			firstAired.set(Calendar.MILLISECOND, 0);
		}

		public long getFirstAired()
		{
			if (firstAired == null) {
				return 0;
			}
			zeroTimeOnFirstAired();
			return firstAired.getTimeInMillis();
		}

		public String getTitle()
		{
			return title;
		}

		Episode getEpisode() {
			return new Episode((short) getSeason(), (short) getEpisodeNumber(),
					new Date(getFirstAired()));
		}
	}

	private static class EpisodeSorter implements Comparator<TVDBEpisode>, Serializable
	{
		private static final long serialVersionUID = 1L;

		public int compare(final TVDBEpisode ep1, final TVDBEpisode ep2)
		{
			final int season1 = ep1.getSeason();
			final int season2 = ep2.getSeason();

			if (season1 != season2) {
				return season1 - season2;
			}

			final int num1 = ep1.getEpisodeNumber();
			final int num2 = ep2.getEpisodeNumber();

			if (num1 != num2) {
				return num1 - num2;
			}
			return 0;
		}

	}

	@XmlElement(name = "Series")
	private TVDBSeries series;

	// This list should stay sorted.
	@XmlElement(name = "Episode")
	private List<TVDBEpisode> episodeList = new ArrayList<TVDBEpisode>();

	public Episode getNextEpisode(Date date)
	{
		long checkDate = date.getValue();
		for (TVDBEpisode e : episodeList) {
			if (e.getFirstAired() > checkDate) {
				return e.getEpisode();
			}
		}
		return null; // TODO: should throw exception
	}

	public Episode getNextEpisode(final Episode last)
	{
		boolean takeThisOne = false;
		for (TVDBEpisode e : episodeList) {
			if (takeThisOne)
				return e.getEpisode();
			else if (e.equalsEpisode(last))
				takeThisOne = true;
		}

		if (takeThisOne == false) {
			throw new RuntimeException("Get Next Episode on episode not in list");
			// TODO: might not be runtime exception, depending on notifying the clients
		}

		// You passed in the last Episode
		return null;
	}

	private void sort(Comparator<TVDBEpisode> comparator) {
		Collections.sort(episodeList, comparator);
	}

	public String getBanner() {
		return series.banner;
	}

	public String getName() {
		return series.name;
	}


	public String getOverview() {
		return series.overview;
	}

	public static FullSeriesRecord create(InputStream is)
	{
		try
		{
			JAXBContext context = JAXBContext.newInstance(FullSeriesRecord.class);
			Unmarshaller um = context.createUnmarshaller();
			FullSeriesRecord record = (FullSeriesRecord) um.unmarshal(is);
			record.sort(new EpisodeSorter());
			return record;
		}
		catch (JAXBException e)
		{
			// If we can't parse the results, return none.
			return new FullSeriesRecord();
		}
	}

}
