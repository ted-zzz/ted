package nu.ted.gwt.client.page.search;

import java.util.List;

import net.bugsquat.gwtsite.client.page.PageId;
import nu.ted.gwt.client.Css;
import nu.ted.gwt.client.TedPageId;
import nu.ted.gwt.client.page.DefaultPage;
import nu.ted.gwt.client.page.SearchResultList;
import nu.ted.gwt.client.widget.table.TedTableModel;
import nu.ted.gwt.domain.FoundSeries;
import nu.ted.gwt.domain.SearchSeriesInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;

public class SearchPage extends DefaultPage<SearchPageController> {

	private final SearchPageController controller;
	private TedTableModel<FoundSeries> searchResultListModel;
	private FlowPanel seriesInfoPanel;
	private SearchResultList searchResultList;
	private ScrollPanel seriesListScrollPanel;
	private Button watchButton;

	public SearchPage(SearchPageController controller)
	{
		this.controller = controller;
		initView();
	}

	@Override
	public SearchPageController getController() {
		return controller;
	}

	@Override
	public PageId getId() {
		return TedPageId.SEARCH;
	}

	@Override
	public void loadData() {
		this.controller.loadData(this);
	}

	private void initView() {
		FlowPanel searchPanel = new FlowPanel();

		final TextBox searchBox = new TextBox();
		// TODO [MS] Get this value from a resource bundle.
		Button searchButton = new Button("Search");
		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				controller.search(searchBox.getText());
			}
		});

		searchPanel.add(searchBox);
		searchPanel.add(searchButton);
		content.add(searchPanel);

		searchResultListModel = new TedTableModel<FoundSeries>();
		searchResultList = new SearchResultList(searchResultListModel);
		searchResultList.addSelectionListener(controller);

		seriesListScrollPanel = new ScrollPanel();
		seriesListScrollPanel.add(searchResultList);
		seriesListScrollPanel.setStyleName(Css.SearchPage.TED_SERIES_LIST);
		seriesListScrollPanel.setVisible(false);


		seriesInfoPanel = new FlowPanel();
		seriesInfoPanel.setStyleName(Css.SearchPage.TED_SEARCH_SERIES_INFO);

		content.add(seriesInfoPanel);
		content.add(seriesListScrollPanel);

		watchButton = new Button("Watch");
		watchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				controller.addSelectedAsWatchedSeries();
			}
		});
		content.add(watchButton);
		watchButton.setVisible(false);
	}

	public void setSearchResults(List<FoundSeries> results)
	{
		seriesListScrollPanel.setVisible(!results.isEmpty());
		watchButton.setVisible(!results.isEmpty());
		searchResultListModel.setData(results);
	}

	@Override
	public String getHeaderText() {
		// TODO [MS] Get this value from a resource bundle.
		return "Search";
	}

	public void setSeriesInfo(SearchSeriesInfo seriesInfo) {
		clearSeriesInfo();
		if (seriesInfo.isImageAdded()) {
			Image image = new Image(GWT.getModuleBaseURL() +
					"images?iid=" + seriesInfo.getSearchUUID());
			image.setStyleName(Css.SearchPage.TED_SEARCH_SERIES_IMG);
			seriesInfoPanel.add(image);
		}
		else {
			seriesInfoPanel.add(new Label("No Series Image Available"));
		}

		Label overview = new Label(seriesInfo.getOverview());
		overview.setStyleName(Css.SearchPage.TED_SEARCH_SERIES_OVERVIEW);
		seriesInfoPanel.add(overview);
	}

	public void clearSeriesInfo() {
		seriesInfoPanel.clear();
	}

	public boolean hasSelectedResult() {
		return !(this.searchResultList.getSelectedRow() < 0);
	}

	public FoundSeries getSelectedResult() {
		return this.searchResultListModel.getSerie(this.searchResultList.getSelectedRow());
	}

}
