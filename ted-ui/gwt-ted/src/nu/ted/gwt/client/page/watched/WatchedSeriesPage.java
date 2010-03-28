package nu.ted.gwt.client.page.watched;

import java.util.List;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import net.bugsquat.gwtsite.client.page.PageId;
import nu.ted.gwt.client.TedPageId;
import nu.ted.gwt.client.page.DefaultPage;
import nu.ted.gwt.domain.GwtWatchedSeries;

public class WatchedSeriesPage extends DefaultPage<WatchedSeriesPageController>{

	private WatchedSeriesPageController controller;
	private FlowPanel watchedSeriesList;

	public WatchedSeriesPage(WatchedSeriesPageController controller) {
		this.controller = controller;

		watchedSeriesList = new FlowPanel();
		content.add(watchedSeriesList);
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

	public void setWatchedSeries(List<GwtWatchedSeries> watchedSeries) {
		this.watchedSeriesList.clear();

		if (watchedSeries.isEmpty()) {
			watchedSeriesList.add(new Label("There are currently no series being watched."));
			return;
		}

		for (GwtWatchedSeries watched : watchedSeries) {
			watchedSeriesList.add(new Label(watched.getName()));
		}
	}

}
