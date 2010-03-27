package nu.ted.domain;

import java.util.Calendar;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import nu.ted.generated.CurrentEpisode;

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

	// Not sure if we want this here or not.
	public CurrentEpisode getCurrentEpisode() {
		return new CurrentEpisode((short) seasonNum, (short) number, DatatypeConverter.printDate(aired));
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
