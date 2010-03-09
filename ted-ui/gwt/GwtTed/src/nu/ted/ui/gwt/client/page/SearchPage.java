package nu.ted.ui.gwt.client.page;

import java.util.List;

import net.bugsquat.gwtsite.client.page.Page;
import net.bugsquat.gwtsite.client.page.PageId;
import nu.ted.ui.gwt.client.TedPageId;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SearchPage extends Page<SearchPageController> {

	private final SearchPageController controller;
	private VerticalPanel searchResults;

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
		HorizontalPanel searchPanel = new HorizontalPanel();
		
		final TextBox searchBox = new TextBox();
		Button searchButton = new Button("Search");
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				controller.search(searchBox.getText());
			}
		});
		
		searchPanel.add(searchBox);
		searchPanel.add(searchButton);
		
		searchResults = new VerticalPanel();
		
		content.add(searchPanel);
		content.add(searchResults);
	}
	
	public void setSearchResults(List<String> results)
	{
		searchResults.clear();
		for (String r : results)
		{
			searchResults.add(new Label(r));
		}
	}
}
