package nu.ted.gwt.client;

import net.bugsquat.gwtsite.client.page.Pages;
import nu.ted.gwt.client.page.search.SearchPage;
import nu.ted.gwt.client.page.search.SearchPageController;

public class TedPages extends Pages {

	public TedPages() {
		registerPage(createSearchPage());
	}

	private SearchPage createSearchPage()
	{
		SearchPageController controller = new SearchPageController();
		return controller.getPage();
	}
}
