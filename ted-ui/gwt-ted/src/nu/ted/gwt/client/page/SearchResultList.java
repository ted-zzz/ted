package nu.ted.gwt.client.page;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import nu.ted.gwt.client.widget.table.TedTable;
import nu.ted.gwt.client.widget.table.TedTableModel;
import nu.ted.gwt.domain.FoundSeries;

public class SearchResultList extends TedTable<FoundSeries> {

	public SearchResultList(TedTableModel<FoundSeries> model) {
		super(model);
	}

	@Override
	protected Widget createWidgetForRow(FoundSeries result) {
		return new Label("[" + result.getSearchId() + "] " + result.getName());
	}

}
