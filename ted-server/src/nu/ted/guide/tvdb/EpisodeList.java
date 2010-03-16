package nu.ted.guide.tvdb;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

	@XmlElement(name = "Episode")
	private List<Episode> episodeList = new ArrayList<Episode>();

	public List<Episode> getEpisodeList()
	{
		return episodeList;
	}

	public static EpisodeList create(InputStream is)
	{
		try
		{
			JAXBContext context = JAXBContext.newInstance(EpisodeList.class);
			Unmarshaller um = context.createUnmarshaller();
			return (EpisodeList) um.unmarshal(is);
		}
		catch (JAXBException e)
		{
			// TODO: fix logging
			// TedLog.getInstance().error(e, "Unable to parse episodelist");
			return new EpisodeList();
		}
	}

}
