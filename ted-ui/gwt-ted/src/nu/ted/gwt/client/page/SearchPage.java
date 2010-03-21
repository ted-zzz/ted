package nu.ted.gwt.client.page;

import java.util.List;

import net.bugsquat.gwtsite.client.page.PageId;
import nu.ted.gwt.client.TedPageId;
import nu.ted.gwt.client.widget.table.TedTableModel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SearchPage extends DefaultPage<SearchPageController> {

	private final SearchPageController controller;
	private TedTableModel<String> searchResultListModel;
	private FlowPanel showInfoPanel;

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
		
		searchResultListModel = new TedTableModel<String>();
		SearchResultList searchResultList = new SearchResultList(searchResultListModel);

		ScrollPanel showListScrollPanel = new ScrollPanel();
        showListScrollPanel.add(searchResultList);
        showListScrollPanel.setStyleName("ted-show-list");
        
		
		showInfoPanel = new FlowPanel();
		showInfoPanel.setStyleName("ted-search-show-info");
		clearShowInfo();

		content.add(showInfoPanel);
		content.add(showListScrollPanel);
	}

	public void setSearchResults(List<String> results)
	{
		searchResultListModel.setData(results);
	}

	@Override
	public String getHeaderText() {
		// TODO [MS] Get this value from a resource bundle.
		return "Search";
	}
	
	private void setShowInfo() {
		// TODO Auto-generated method stub
		
	}

	private void clearShowInfo() {
		showInfoPanel.clear();
		showInfoPanel.add(new Label("No Show Info Available"));
	}
}
