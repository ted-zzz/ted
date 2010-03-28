package nu.ted.gwt.client;

import net.bugsquat.gwtsite.client.page.PageId;

public class TedPageId implements PageId
{
	public static TedPageId WATCHED_SERIES = new TedPageId();
	public static TedPageId SEARCH = new TedPageId();

	private TedPageId()
	{

	}
}
