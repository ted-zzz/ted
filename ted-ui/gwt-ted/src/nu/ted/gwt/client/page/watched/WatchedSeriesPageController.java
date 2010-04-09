package nu.ted.gwt.client.page.watched;

import java.util.List;

import net.bugsquat.gwtsite.client.PageLoader;
import net.bugsquat.gwtsite.client.page.PageController;
import nu.ted.gwt.client.MessageCallback;
import nu.ted.gwt.client.TedPageId;
import nu.ted.gwt.client.event.EventListener;
import nu.ted.gwt.client.event.EventQueue;
import nu.ted.gwt.domain.GwtWatchedSeries;
import nu.ted.gwt.domain.event.GwtEventType;
import nu.ted.gwt.domain.event.WatchedSeriesEvent;

import com.google.gwt.core.client.GWT;

public class WatchedSeriesPageController extends PageController<WatchedSeriesPage> {

	private final WatchedSeriesServiceAsync watchedSeriesService =
		(WatchedSeriesServiceAsync)GWT.create(WatchedSeriesService.class);

	public WatchedSeriesPageController() {
		this.page = new WatchedSeriesPage(this);

		EventQueue.registerListener(GwtEventType.WATCHED_SERIES_ADDED,
				new EventListener<WatchedSeriesEvent>() {

					@Override
					public void onEvent(WatchedSeriesEvent event) {
						System.out.println("Series added to watched list.");
						addSeriesToList(event.getSeries());
					}
				}
		);

		EventQueue.registerListener(GwtEventType.WATCHED_SERIES_REMOVED,
				new EventListener<WatchedSeriesEvent>() {

					@Override
					public void onEvent(WatchedSeriesEvent event) {
						System.out.println("Series removed from watched list.");
						removeSeriesFromList(event.getSeries());
					}
				}
		);
	}

	@Override
	public WatchedSeriesPage getPage() {
		return this.page;
	}

	public void loadData() {
		watchedSeriesService.getWatchedSeries(new MessageCallback<List<GwtWatchedSeries>>() {
			@Override
			public void onSuccess(List<GwtWatchedSeries> watchedSeries) {
				page.setWatchedSeries(watchedSeries);
			}
		});
		loadListener.pageDataHasBeenLoaded(page);
	}

	public void stopWatching(short seriesUid) {
		// TODO [MS] Add a prompt here, providing a chance to cancel.
		PageLoader.getInstance().showLoadingPage();
		watchedSeriesService.stopWatching(seriesUid, new MessageCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				PageLoader.getInstance().hideLoadingPage();
				PageLoader.getInstance().loadPage(TedPageId.WATCHED_SERIES);
			}
		});
	}

	private void addSeriesToList(GwtWatchedSeries series) {
		// TODO [MS] Check to see if it already exists in the page's list.
		// TODO [MS] If not, get the data from the server, and add it to the page.
	}

	private void removeSeriesFromList(GwtWatchedSeries series) {
		page.removeSeriesFromList(series.getuID());
	}
}
