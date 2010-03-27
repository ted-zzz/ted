package nu.ted.gwt.client.page;

import java.util.List;

import net.bugsquat.gwtsite.client.PageLoader;
import net.bugsquat.gwtsite.client.page.Page;
import net.bugsquat.gwtsite.client.page.PageController;
import nu.ted.gwt.client.MessageCallback;
import nu.ted.gwt.client.widget.table.TedTableSelectionListener;
import nu.ted.gwt.domain.FoundSeries;
import nu.ted.gwt.domain.SearchSeriesInfo;

import com.google.gwt.core.client.GWT;

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
		searchService.search(filter, new MessageCallback<List<FoundSeries>>() {

			@Override
			public void onSuccess(List<FoundSeries> results) {
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

		page.clearSeriesInfo();

		FoundSeries selected = page.getSelectedResult();
		searchService.getSeriesInfo(selected.getSearchId(), new MessageCallback<SearchSeriesInfo>() {

			@Override
			public void onSuccess(SearchSeriesInfo seriesInfo) {
				page.setSeriesInfo(seriesInfo);
			}
		});

	}

}
