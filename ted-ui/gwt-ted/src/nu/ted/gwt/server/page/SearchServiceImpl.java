package nu.ted.gwt.server.page;

import java.util.List;

import org.apache.thrift.TException;

import net.bugsquat.diservlet.ImageServlet;
import net.bugsquat.diservlet.ImageStore;
import nu.ted.client.ClientAction;
import nu.ted.client.JavaClient;
import nu.ted.generated.TedService.Client;
import nu.ted.gwt.client.page.SearchService;
import nu.ted.gwt.domain.SearchShowInfo;
import nu.ted.gwt.domain.ShowSearchResult;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SearchServiceImpl extends RemoteServiceServlet implements
		SearchService {

	@Override
	public List<ShowSearchResult> search(final String filter) {
		JavaClient tedClient = getTedClient();
		SearchClientAction searchAction = new SearchClientAction(filter);
		tedClient.run(searchAction);
		return searchAction.getFoundSeries();
	}

	@Override
	public SearchShowInfo getShowInfo(final String searchUUID) {
		JavaClient tedClient = getTedClient();
		GetOverviewClientAction overviewAction = new GetOverviewClientAction(searchUUID);
		tedClient.run(overviewAction);
		String overview = overviewAction.getOverview();

		ImageStore store = getImageStore();
		if (store == null) {
			// Allow the client to continue without showing an image.
			return new SearchShowInfo(searchUUID, overview, false);
		}

		if (store.contains(searchUUID)) {
			return new SearchShowInfo(searchUUID, overview, true);
		}


		LoadBannerClientAction action = new LoadBannerClientAction(searchUUID, store);
		tedClient.run(action);

		if (!action.imageAdded()) {
			return new SearchShowInfo(searchUUID, overview, false);
		}


		return new SearchShowInfo(searchUUID, overview, true);
	}

	private JavaClient getTedClient() {
		return new JavaClient("localhost", 9030);
	}

	private ImageStore getImageStore() {
		return (ImageStore) getServletContext().getAttribute(ImageServlet.IMAGE_STORE_ATTR_KEY);
	}

}
