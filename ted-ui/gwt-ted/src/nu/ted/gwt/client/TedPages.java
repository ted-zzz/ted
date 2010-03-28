package nu.ted.gwt.client;

import net.bugsquat.gwtsite.client.page.Page;
import net.bugsquat.gwtsite.client.page.PageController;
import net.bugsquat.gwtsite.client.page.Pages;
import nu.ted.gwt.client.page.search.SearchPage;
import nu.ted.gwt.client.page.search.SearchPageController;
import nu.ted.gwt.client.page.watched.WatchedSeriesPageController;

public class TedPages extends Pages {

	public TedPages() {
		registerPage(createWatchedSeriesPage());
		registerPage(createSearchPage());
	}

	private Page<? extends PageController> createWatchedSeriesPage() {
		WatchedSeriesPageController controller = new WatchedSeriesPageController();
		return controller.getPage();
	}

	private SearchPage createSearchPage()
	{
		SearchPageController controller = new SearchPageController();
		return controller.getPage();
	}
}
