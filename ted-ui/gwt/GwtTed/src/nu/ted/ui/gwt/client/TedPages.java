package nu.ted.ui.gwt.client;

import net.bugsquat.gwtsite.client.page.Pages;
import nu.ted.ui.gwt.client.page.SearchPage;
import nu.ted.ui.gwt.client.page.SearchPageController;

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
