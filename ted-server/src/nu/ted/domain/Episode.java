package nu.ted.domain;

import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Episode
{
	@XmlElement
	private int seasonNum;

	@XmlElement
	private int number;

	@XmlElement
	private Calendar aired;

	public Episode(int seasonNum, int number, Calendar aired)
	{
		super();
		this.seasonNum = seasonNum;
		this.number = number;
		this.aired = aired;
	}

	public int getSeasonNum()
	{
		return seasonNum;
	}

	public int getEpisodeNum()
	{
		return number;
	}

	public Calendar getAired()
	{
		return aired;
	}

}
