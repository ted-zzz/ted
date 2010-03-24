package nu.ted.gwt.client.page;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import nu.ted.gwt.client.widget.table.TedTable;
import nu.ted.gwt.client.widget.table.TedTableModel;
import nu.ted.gwt.domain.ShowSearchResult;

public class SearchResultList extends TedTable<ShowSearchResult> {

	public SearchResultList(TedTableModel<ShowSearchResult> model) {
		super(model);
	}

	@Override
	protected Widget createWidgetForRow(ShowSearchResult result) {
		return new Label("[" + result.getSearchId() + "] " + result.getName());
	}

}
