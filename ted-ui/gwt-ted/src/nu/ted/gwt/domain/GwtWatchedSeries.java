package nu.ted.gwt.domain;

import java.io.Serializable;
import java.util.List;

public class GwtWatchedSeries implements Serializable {

	private static final long serialVersionUID = 1L;

	private short uID;
	private String name;

	private List<GwtEpisode> episodes;

	// Required by GWT.
	public GwtWatchedSeries(){}

	public GwtWatchedSeries(short uID, String name, List<GwtEpisode> episodes) {
		this.uID = uID;
		this.name = name;
		this.episodes = episodes;
	}

	public short getuID() {
		return uID;
	}

	public void setuID(short uID) {
		this.uID = uID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEpisodes(List<GwtEpisode> episodes) {
		this.episodes = episodes;
	}

	public List<GwtEpisode> getEpisodes() {
		return episodes;
	}

}
