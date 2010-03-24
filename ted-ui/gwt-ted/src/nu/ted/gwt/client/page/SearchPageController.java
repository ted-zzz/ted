package nu.ted.gwt.client.page;

import java.util.List;

import net.bugsquat.gwtsite.client.PageLoader;
import net.bugsquat.gwtsite.client.page.Page;
import net.bugsquat.gwtsite.client.page.PageController;
import nu.ted.gwt.client.MessageCallback;
import nu.ted.gwt.client.widget.table.TedTableSelectionListener;
import nu.ted.gwt.domain.SearchShowInfo;
import nu.ted.gwt.domain.ShowSearchResult;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SearchPageController extends PageController<SearchPage> implements TedTableSelectionListener
{
    private final SearchServiceAsync searchService = (SearchServiceAsync)GWT.create(SearchService.class);

    public SearchPageController() {
        this.page = new SearchPage(this);
    }

    public void loadData(Page<SearchPageController> page) {
        loadListener.pageDataHasBeenLoaded(page);
    }

    public void search(String filter) {
        PageLoader.getInstance().showLoadingPage();
        searchService.search(filter, new MessageCallback<List<ShowSearchResult>>() {

            @Override
            public void onSuccess(List<ShowSearchResult> results) {
                hideLoadingIndicator();
                page.setSearchResults(results);
            }

            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
                hideLoadingIndicator();
            }

            private void hideLoadingIndicator() {
                PageLoader.getInstance().hideLoadingPage();
            }
        });

    }

    @Override
    public SearchPage getPage() {
        return page;
    }

    @Override
    public void selectionChanged() {
        if (!page.hasSelectedResult())
            return;

        page.clearShowInfo();

        ShowSearchResult selected = page.getSelectedResult();
        searchService.loadBanner(selected.getSearchId(), new MessageCallback<SearchShowInfo>() {

            @Override
            public void onSuccess(SearchShowInfo imageAdded) {
                if (imageAdded.isImageAdded()) {
                    page.setShowInfo(imageAdded.getSearchUUID());
                }
                else {
                    // TODO [MS] Might be better to show a default image
                    //           stating no image data found.
                    page.showNoShowInfoAvailable();
                }
            }
        });

    }

}
