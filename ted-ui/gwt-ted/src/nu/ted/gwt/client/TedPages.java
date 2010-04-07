package nu.ted.gwt.client;

import net.bugsquat.gwtsite.client.page.PageControllerRegistry;
import nu.ted.gwt.client.page.search.SearchPageController;
import nu.ted.gwt.client.page.watched.WatchedSeriesPageController;

public class TedPages extends PageControllerRegistry {

	public TedPages() {
		registerPageController(new WatchedSeriesPageController());
		registerPageController(new SearchPageController());
	}

}
