package nu.ted.gwt.domain;

import java.io.Serializable;

public class GwtWatchedSeries implements Serializable {

	private String name;

	// Required by GWT.
	public GwtWatchedSeries(){}

	public GwtWatchedSeries(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
