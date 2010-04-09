package nu.ted.gwt.client;

import net.bugsquat.gwtsite.client.PageLoader;
import net.bugsquat.gwtsite.client.page.PageId;
import nu.ted.gwt.client.event.EventListener;
import nu.ted.gwt.client.event.EventQueue;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtTed implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		EventQueue.addListener(new EventListener() {

			@Override
			public void onEvent() {
				System.out.println("Processing event...");
			}
		});
		PageLoader.getInstance().loadPage(TedPageId.SEARCH);
	}
}
