package nu.ted.guide.tvdb;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Data")
public class EpisodeList
{
	public static class Episode
	{
		@XmlElement(name = "SeasonNumber")
		private int season;

		@XmlElement(name = "EpisodeNumber")
		private int episode;

		@XmlElement(name = "FirstAired")
		private Calendar firstAired;

		@XmlElement(name = "EpisodeName")
		private String title;

		public int getSeason()
		{
			return season;
		}

		public int getEpisode()
		{
			return episode;
		}

		public Calendar getFirstAired()
		{
			return firstAired;
		}

		public Date getFirstAiredDate()
		{
			return firstAired.getTime();
		}

		public String getTitle()
		{
			return title;
		}
	}

	// This list should stay sorted.
	@XmlElement(name = "Episode")
	private List<Episode> episodeList = new ArrayList<Episode>();

	private static class EpisodeSorter implements Comparator<Episode>
	{
		public int compare(final Episode ep1, final Episode ep2)
		{
			final int season1 = ep1.getSeason();
			final int season2 = ep2.getEpisode();
			
			if (season1 != season2) {
				return season1 - season2;
			}
			
			final int num1 = ep1.getEpisode();
			final int num2 = ep2.getEpisode();
			
			if (num1 != num2) {
				return num1 - num2;
			}
			return 0;
		}
		
	}
	
	public Episode getNextEpisode()
	{
		Calendar now = Calendar.getInstance();
		for (Episode e : episodeList) {
			if (e.getFirstAired().after(now))
				return e;
		}
		return null; // TODO: should throw exception
	}

	private void sort(Comparator<Episode> comparator) {
		Collections.sort(episodeList, comparator);
	}

	public static EpisodeList create(InputStream is)
	{
		try
		{
			JAXBContext context = JAXBContext.newInstance(EpisodeList.class);
			Unmarshaller um = context.createUnmarshaller();
			EpisodeList eList = (EpisodeList) um.unmarshal(is);
			eList.sort(new EpisodeSorter());
			return eList;
		}
		catch (JAXBException e)
		{
			// TODO: fix logging
			// TedLog.getInstance().error(e, "Unable to parse episodelist");
			return new EpisodeList();
		}
	}
}
