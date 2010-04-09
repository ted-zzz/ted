package nu.ted.gwt.client;

import net.bugsquat.gwtsite.client.PageLoader;
import nu.ted.gwt.client.event.EventListener;
import nu.ted.gwt.client.event.EventQueue;
import nu.ted.gwt.domain.event.GwtEventType;
import nu.ted.gwt.domain.event.WatchedSeriesEvent;

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtTed implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		EventQueue.registerListener(GwtEventType.WATCHED_SERIES_ADDED,
				new EventListener<WatchedSeriesEvent>() {

					@Override
					public void onEvent(WatchedSeriesEvent event) {
						System.out.println("Series added to watched list.");
					}
				}
		);

		EventQueue.registerListener(GwtEventType.WATCHED_SERIES_REMOVED,
				new EventListener<WatchedSeriesEvent>() {

					@Override
					public void onEvent(WatchedSeriesEvent event) {
						System.out.println("Series removed from watched list.");
					}
				}
		);

		PageLoader.getInstance().loadPage(TedPageId.SEARCH);
	}
}
