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
	private short uid;

	@XmlElement
	private Episode lastAired;

	// TODO: this should only be settable in constructor
	public void setUID(short uid)
	{
		this.uid = uid;
	}

	public short getUid()
	{
		return uid;
	}

	public Series(short uid, String name) {
		this.name = name;
		this.uid = uid;
	}

	public String getName() {
		return name;
	}
}
