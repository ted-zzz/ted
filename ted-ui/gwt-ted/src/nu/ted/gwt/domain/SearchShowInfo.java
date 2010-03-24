package nu.ted.gwt.domain;

import java.io.Serializable;

/**
 * Series information provided by the server and shown in the search page.
 *
 */
public class SearchShowInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean imageAdded;
    private String searchUUID;
    private String mimeType;

    public SearchShowInfo(){}

    public SearchShowInfo(String searchUUID, boolean imageAdded) {
        this.searchUUID = searchUUID;
        this.imageAdded = imageAdded;
    }

    public String getSearchUUID() {
        return searchUUID;
    }

    public void setSearchUUID(String searchUUID) {
        this.searchUUID = searchUUID;
    }

    public boolean isImageAdded() {
        return imageAdded;
    }

    public void setImageAdded(boolean imageAdded) {
        this.imageAdded = imageAdded;
    }

}
