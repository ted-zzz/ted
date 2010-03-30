package nu.ted.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import nu.ted.generated.AiredEpisode;
import nu.ted.generated.CurrentEpisode;
import nu.ted.generated.EpisodeStatus;
import nu.ted.generated.WatchedSeries;
import nu.ted.guide.GuideDB;
import nu.ted.guide.GuideFactory;

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

	public Series(Calendar date, short uid, GuideDB guide, String guideId) {
		this.uid = uid;
		this.guideDB = guide;
		this.guideDBName = guide.getName();
		this.guideID = guideId;

		this.name = guide.getName(guideId);
		this.currentEpisode = guide.getLastEpisode(this.guideID, date);
	}

	public WatchedSeries getWatchedSeries() {
		CurrentEpisode episode = currentEpisode.getCurrentEpisode();
		List<AiredEpisode> aired = new LinkedList<AiredEpisode>();
		for (Episode e : episodes) {
			aired.add(new AiredEpisode((short) e.getSeasonNum(), (short)e.getEpisodeNum(),
					DatatypeConverter.printDate(e.getAired()), EpisodeStatus.SEARCHING));
		}
		return new WatchedSeries(uid, name, episode, aired);
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

	public void update(Calendar calendar) {
		List<Episode> newEpisodes = getGuide().getNewAiredEpisodes(guideID, calendar, currentEpisode);

		for (Episode e : newEpisodes) {
			episodes.add(e);
		}

		// TODO: process Episodes
	}

	private GuideDB getGuide() {
		if (guideDB == null)
			guideDB = GuideFactory.getGuide(guideDBName);

		return guideDB;
	}
}
