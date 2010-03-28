package nu.ted.gwt.client.page.watched;

import java.util.List;

import net.bugsquat.gwtsite.client.page.PageController;
import nu.ted.gwt.client.MessageCallback;
import nu.ted.gwt.domain.GwtWatchedSeries;

import com.google.gwt.core.client.GWT;

public class WatchedSeriesPageController extends PageController<WatchedSeriesPage> {

	private final WatchedSeriesServiceAsync watchedSeriesService =
		(WatchedSeriesServiceAsync)GWT.create(WatchedSeriesService.class);

	private WatchedSeriesPage page;

	public WatchedSeriesPageController() {
		this.page = new WatchedSeriesPage(this);
	}

	@Override
	public WatchedSeriesPage getPage() {
		return this.page;
	}

	public void loadData(WatchedSeriesPage watchedSeriesPage) {
		watchedSeriesService.getWatchedSeries(new MessageCallback<List<GwtWatchedSeries>>() {
			@Override
			public void onSuccess(List<GwtWatchedSeries> watchedSeries) {
				page.setWatchedSeries(watchedSeries);
			}
		});
		loadListener.pageDataHasBeenLoaded(page);
	}

}
