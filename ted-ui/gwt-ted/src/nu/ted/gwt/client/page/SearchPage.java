package nu.ted.gwt.client.page;

import java.util.List;

import net.bugsquat.gwtsite.client.page.PageId;
import nu.ted.gwt.client.TedPageId;
import nu.ted.gwt.client.widget.table.TedTableModel;
import nu.ted.gwt.domain.ShowSearchResult;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SearchPage extends DefaultPage<SearchPageController> {

    private final SearchPageController controller;
    private TedTableModel<ShowSearchResult> searchResultListModel;
    private FlowPanel showInfoPanel;
    private SearchResultList searchResultList;

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

        searchResultListModel = new TedTableModel<ShowSearchResult>();
        searchResultList = new SearchResultList(searchResultListModel);
        searchResultList.addSelectionListener(controller);

        ScrollPanel showListScrollPanel = new ScrollPanel();
        showListScrollPanel.add(searchResultList);
        showListScrollPanel.setStyleName("ted-show-list");


        showInfoPanel = new FlowPanel();
        showInfoPanel.setStyleName("ted-search-show-info");
        showNoShowInfoAvailable();

        content.add(showInfoPanel);
        content.add(showListScrollPanel);
    }

    public void setSearchResults(List<ShowSearchResult> results)
    {
        searchResultListModel.setData(results);
    }

    @Override
    public String getHeaderText() {
        // TODO [MS] Get this value from a resource bundle.
        return "Search";
    }

    public void setShowInfo(String searchUUID) {
        clearShowInfo();
        Image image = new Image(GWT.getModuleBaseURL() + "images?iid=" + searchUUID);
        image.setStyleName("ted-search-serie-img");
        showInfoPanel.add(image);
    }

    public void showNoShowInfoAvailable() {
        clearShowInfo();
        showInfoPanel.add(new Label("No Show Info Available"));
    }

    public void clearShowInfo() {
        showInfoPanel.clear();
    }

    public boolean hasSelectedResult() {
        return !(this.searchResultList.getSelectedRow() < 0);
    }

    public ShowSearchResult getSelectedResult() {
        return this.searchResultListModel.getSerie(this.searchResultList.getSelectedRow());
    }
}
