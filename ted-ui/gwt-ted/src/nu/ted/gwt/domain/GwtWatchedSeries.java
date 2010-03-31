package nu.ted.gwt.domain;

import java.io.Serializable;

public class GwtWatchedSeries implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private short uID;
	private String name;

	// Required by GWT.
	public GwtWatchedSeries(){}

	public GwtWatchedSeries(short uID, String name) {
		this.uID = uID;
		this.name = name;
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

}
