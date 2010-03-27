package nu.ted.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import nu.ted.generated.AiredEpisode;
import nu.ted.generated.CurrentEpisode;
import nu.ted.generated.WatchedSeries;
import nu.ted.guide.GuideDB;

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
	private Episode currentEpisode;

	// List of Episodes that have aired and possible been found
	@XmlElement
	private List<Episode> episodes = new ArrayList<Episode>();

	@XmlElement
	private String guideID;
	@XmlElement
	private String guideDBName;

	// Do not persist:
	private GuideDB guideDB;

	public Series(short uid, GuideDB guide, String guideId) {
		this.uid = uid;
		this.guideDB = guide;
		this.guideDBName = guide.getName();
		this.guideID = guideId;

		this.name = guide.getName(guideId);
		this.currentEpisode = guide.getLastEpisode(this.guideID, Calendar.getInstance());
	}

	public WatchedSeries getWatchedSeries() {
		CurrentEpisode episode = currentEpisode.getCurrentEpisode();
		return new WatchedSeries(uid, name, episode, new ArrayList<AiredEpisode>());
	}
	public short getUID()
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

	public List<Episode> getEpisodes() {
		return Collections.unmodifiableList(episodes);
	}
}
