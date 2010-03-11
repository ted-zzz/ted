package nu.ted.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a representation of a TV Series.
 *
 */
@XmlRootElement
public class Series
{
	@XmlElement
	private String name;

	@XmlElement
	private String uid;

	@XmlElement
	private Episode lastAired;

	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

	public Series(String uid, String name) {
		this.name = name;
		this.uid = uid;
	}

	public String getName() {
		return name;
	}
}
