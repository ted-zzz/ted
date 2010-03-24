package nu.ted.gwt.server.page;

import java.util.List;

import net.bugsquat.diservlet.ImageServlet;
import net.bugsquat.diservlet.ImageStore;
import nu.ted.client.JavaClient;
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
    public SearchShowInfo loadBanner(final String searchUUID) {
        ImageStore store = getImageStore();
        if (store == null) {
            // Allow the client to continue without showing an image.
            return new SearchShowInfo(searchUUID, false);
        }

        if (store.contains(searchUUID)) {
            return new SearchShowInfo(searchUUID, true);
        }

        JavaClient tedClient = getTedClient();
        LoadBannerClientAction action = new LoadBannerClientAction(searchUUID, store);
        tedClient.run(action);

        if (!action.imageAdded()) {
            return new SearchShowInfo(searchUUID, false);
        }


        return new SearchShowInfo(searchUUID, true);
    }

    private JavaClient getTedClient() {
        return new JavaClient("localhost", 9030);
    }

    private ImageStore getImageStore() {
        return (ImageStore) getServletContext().getAttribute(ImageServlet.IMAGE_STORE_ATTR_KEY);
    }

}
