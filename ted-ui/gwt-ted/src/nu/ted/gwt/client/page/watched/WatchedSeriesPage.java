package nu.ted.gwt.client.page.watched;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import net.bugsquat.gwtsite.client.page.PageId;
import nu.ted.gwt.client.TedPageId;
import nu.ted.gwt.client.page.DefaultPage;
import nu.ted.gwt.domain.GwtWatchedSeries;

public class WatchedSeriesPage extends DefaultPage {

	private WatchedSeriesPageController controller;
	private FlowPanel watchedSeriesList;
	private Map<Short, WatchedSeriesWidget> watchedUIDToPanel;

	public WatchedSeriesPage(WatchedSeriesPageController controller) {
		this.controller = controller;

		watchedSeriesList = new FlowPanel();
		watchedSeriesList.setStyleName("ted-watched-list");
		content.add(watchedSeriesList);

		watchedUIDToPanel = new HashMap<Short, WatchedSeriesWidget>();
	}

	@Override
	public String getHeaderText() {
		return "Watched Series";
	}

	@Override
	public PageId getId() {
		return TedPageId.WATCHED_SERIES;
	}

	public void setWatchedSeries(List<GwtWatchedSeries> watchedSeries) {
		clearPage();

		if (watchedSeries.isEmpty()) {
			showNoWatchedSeriesMessage();
			return;
		}

		for (final GwtWatchedSeries watched : watchedSeries) {
			addWatchedSeriesPanel(watched);
		}
	}

	/**
	 * If the series exists, remove it from the list.
	 *
	 * @param getuID the UID of the series to be removed.
	 */
	public void removeSeriesFromList(Short seriesUID) {
		if (watchedUIDToPanel.containsKey(seriesUID)) {
			watchedUIDToPanel.get(seriesUID).removeFromParent();
			watchedUIDToPanel.remove(seriesUID);

			if (watchedUIDToPanel.isEmpty()) {
				clearPage();
				showNoWatchedSeriesMessage();
			}
		}
	}

	public boolean isShowingSeries(short seriesUid) {
		return watchedUIDToPanel.containsKey(seriesUid);
	}

	public void addSeriesToList(GwtWatchedSeries watched) {
		if (!isShowingSeries(watched.getuID())) {

			if (watchedUIDToPanel.isEmpty()) {
				clearPage();
			}

			addWatchedSeriesPanel(watched);
		}
	}

	private void addWatchedSeriesPanel(final GwtWatchedSeries watched) {
		ClickHandler stopWatchingClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				controller.stopWatching(watched.getuID());
			}
		};

		WatchedSeriesWidget watchedSeriesWidget =
			new WatchedSeriesWidget(watched, stopWatchingClickHandler);
		watchedUIDToPanel.put(watched.getuID(), watchedSeriesWidget);
		watchedSeriesList.add(watchedSeriesWidget);
	}

	private void showNoWatchedSeriesMessage() {
		watchedSeriesList.add(new Label("There are currently no series being watched."));
	}

	private void clearPage() {
		this.watchedUIDToPanel.clear();
		this.watchedSeriesList.clear();
	}
}
