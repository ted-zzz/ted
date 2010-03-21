package nu.ted.gwt.client.page;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import nu.ted.gwt.client.widget.table.TedTable;
import nu.ted.gwt.client.widget.table.TedTableModel;

public class SearchResultList extends TedTable<String> {

	public SearchResultList(TedTableModel<String> model) {
		super(model);
	}

	@Override
	protected Widget createWidgetForRow(String showTitle) {
		return new Label(showTitle);
	}

}
