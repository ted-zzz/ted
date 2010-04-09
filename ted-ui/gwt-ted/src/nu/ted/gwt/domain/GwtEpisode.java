package nu.ted.gwt.domain;

import java.io.Serializable;
import java.util.Date;

public class GwtEpisode implements Serializable {
	private short season;
	private short number;
	private Date aired;
	private GwtEpisodeStatus status;

	// Required by GWT
	protected GwtEpisode() {}

	public GwtEpisode(short season, short number, Date aired, GwtEpisodeStatus status) {
		this.season = season;
		this.number = number;
		this.aired = aired;
		this.status = status;
	}

	public short getSeason() {
		return season;
	}
	public void setSeason(short season) {
		this.season = season;
	}
	public short getNumber() {
		return number;
	}
	public void setNumber(short number) {
		this.number = number;
	}
	public Date getAired() {
		return aired;
	}
	public void setAired(Date aired) {
		this.aired = aired;
	}
	public GwtEpisodeStatus getStatus() {
		return status;
	}
	public void setStatus(GwtEpisodeStatus status) {
		this.status = status;
	}

}
