package nu.ted.gwt.client;

import net.bugsquat.gwtsite.client.PageLoader;
import net.bugsquat.gwtsite.client.page.PageId;

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtTed implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		PageLoader.getInstance().loadPage(TedPageId.SEARCH);
	}
}
