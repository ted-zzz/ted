package nu.ted.domain;

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
	private Date aired;

	public Episode(int seasonNum, int number, Date aired)
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

	public int getNumber()
	{
		return number;
	}

	public Date getAired()
	{
		return aired;
	}

}
