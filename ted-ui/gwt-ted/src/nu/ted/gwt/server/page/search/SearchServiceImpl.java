package nu.ted.gwt.server.page.search;

import java.util.List;

import org.apache.thrift.TException;

import net.bugsquat.diservlet.ImageServlet;
import net.bugsquat.diservlet.ImageStore;
import nu.ted.client.ClientAction;
import nu.ted.client.JavaClient;
import nu.ted.generated.TedService.Client;
import nu.ted.gwt.client.page.search.SearchService;
import nu.ted.gwt.domain.SearchSeriesInfo;
import nu.ted.gwt.domain.FoundSeries;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SearchServiceImpl extends RemoteServiceServlet implements
		SearchService {

	@Override
	public List<FoundSeries> search(final String filter) {
		JavaClient tedClient = getTedClient();
		SearchClientAction searchAction = new SearchClientAction(filter);
		tedClient.run(searchAction);
		return searchAction.getFoundSeries();
		// TODO [MS] Should we filter watched shows out of the list?
	}

	@Override
	public SearchSeriesInfo getSeriesInfo(final String searchUUID) {
		JavaClient tedClient = getTedClient();
		GetOverviewClientAction overviewAction = new GetOverviewClientAction(searchUUID);
		tedClient.run(overviewAction);
		String overview = overviewAction.getOverview();

		ImageStore store = getImageStore();
		if (store == null) {
			// Allow the client to continue without showing an image.
			return new SearchSeriesInfo(searchUUID, overview, false);
		}

		if (store.contains(searchUUID)) {
			return new SearchSeriesInfo(searchUUID, overview, true);
		}


		LoadBannerClientAction action = new LoadBannerClientAction(searchUUID, store);
		tedClient.run(action);

		if (!action.imageAdded()) {
			return new SearchSeriesInfo(searchUUID, overview, false);
		}


		return new SearchSeriesInfo(searchUUID, overview, true);
	}

	@Override
	public void addWatchedSeries(String searchId) {
		JavaClient tedClient = getTedClient();
		AddWatchedSeriesClientAction addWatched = new AddWatchedSeriesClientAction(searchId);
		tedClient.run(addWatched);
		// TODO [MS] Handle case where an exception is thrown.
	}

	private JavaClient getTedClient() {
		return new JavaClient("localhost", 9030);
	}

	private ImageStore getImageStore() {
		return (ImageStore) getServletContext().getAttribute(ImageServlet.IMAGE_STORE_ATTR_KEY);
	}

}
