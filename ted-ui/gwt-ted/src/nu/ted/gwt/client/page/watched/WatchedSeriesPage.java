package nu.ted.gwt.client.page.watched;

import net.bugsquat.gwtsite.client.page.PageId;
import nu.ted.gwt.client.TedPageId;
import nu.ted.gwt.client.page.DefaultPage;

public class WatchedSeriesPage extends DefaultPage<WatchedSeriesPageController>{

	private WatchedSeriesPageController controller;

	public WatchedSeriesPage(WatchedSeriesPageController controller) {
		this.controller = controller;
	}

	@Override
	public String getHeaderText() {
		return "Watched Series";
	}

	@Override
	public WatchedSeriesPageController getController() {
		return this.controller;
	}

	@Override
	public PageId getId() {
		return TedPageId.WATCHED_SERIES;
	}

	@Override
	public void loadData() {
		controller.loadData(this);
	}

}
