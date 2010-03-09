package nu.ted.ui.gwt.client.page;

import java.util.ArrayList;

import net.bugsquat.gwtsite.client.page.Page;
import net.bugsquat.gwtsite.client.page.PageController;

public class SearchPageController extends PageController<SearchPage>
{

	public SearchPageController() {
		this.page = new SearchPage(this);
	}

	public void loadData(Page<SearchPageController> page) {
		loadListener.pageDataHasBeenLoaded(page);
	}

	public void search(String text) {
		ArrayList<String> results = new ArrayList<String>();
		results.add("one");
		results.add("two");
		page.setSearchResults(results);
	}

	@Override
	public SearchPage getPage() {
		return page;
	}

}
