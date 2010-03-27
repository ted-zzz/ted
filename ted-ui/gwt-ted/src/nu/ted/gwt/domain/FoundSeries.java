package nu.ted.gwt.domain;

import java.io.Serializable;

/**
 * Represents a search result from the server.
 *
 */
public class FoundSeries implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String searchId;

	/**
	 * Required by GWT.
	 */
	public FoundSeries(){}
	
	public FoundSeries(String name, String searchId) {
		this.name = name;
		this.searchId = searchId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSearchId() {
		return searchId;
	}

	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}
	
}
