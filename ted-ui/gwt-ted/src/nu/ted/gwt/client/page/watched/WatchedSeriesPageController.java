package nu.ted.gwt.client.page.watched;

import net.bugsquat.gwtsite.client.page.PageController;

public class WatchedSeriesPageController extends PageController<WatchedSeriesPage> {

	private WatchedSeriesPage page;

	public WatchedSeriesPageController() {
		this.page = new WatchedSeriesPage(this);
	}

	@Override
	public WatchedSeriesPage getPage() {
		return this.page;
	}

	public void loadData(WatchedSeriesPage watchedSeriesPage) {
		loadListener.pageDataHasBeenLoaded(page);
	}

}
