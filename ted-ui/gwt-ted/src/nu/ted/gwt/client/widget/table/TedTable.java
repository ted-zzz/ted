package nu.ted.gwt.client.widget.table;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

public abstract class TedTable<T> extends Grid implements TedTableModelListener, ClickHandler {

    protected TedTableModel<T> model;
    protected List<TedTableSelectionListener> selectionListeners = new ArrayList<TedTableSelectionListener>();
    protected int selectedRow = -1;

    public TedTable(TedTableModel<T> model) {
        setStyleName("ted-serie-table");
        this.model = model;
        this.model.addModelListener(this);
        addClickHandler(this);
    }

    /**
     * Called when model data is cleared.
     */
    public void dataCleared() {
        while (getRowCount() > 0) {
            removeRow(getRowCount() - 1);
        }
        selectedRow = -1;
    }

    /**
     * Called when the model data has changed.
     */
    public void modelChanged() {
        buildTableFromModel();
    }

    public void addSelectionListener(TedTableSelectionListener listener) {
        if (!this.selectionListeners.contains(listener)) {
            this.selectionListeners.add(listener);
        }
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public boolean hasSelection() {
        return this.selectedRow > -1;
    }

    public void onClick(ClickEvent event) {
        Cell cell = getCellForEvent(event);

        // If user clicks on exact pixel that separates row,
        // don't change selection.
        if (cell == null) {
            return;
        }

        int newSelection = cell.getRowIndex();
        setSelectedRow(newSelection);
    }

    public void setSelectedRow(int rowToSelect) {
        String selectedStyleName = "ted-table-row-selected";

        // Do nothing if we've selected the same index.
        if (this.selectedRow == rowToSelect) {
            return;
        }
        int oldSelection = this.selectedRow;
        this.selectedRow = rowToSelect;

        // Update the CSS Styles.
        if (this.selectedRow >= 0) {
            getRowFormatter().addStyleName(this.selectedRow, selectedStyleName);
        }

        if (oldSelection >= 0) {
            getRowFormatter().removeStyleName(oldSelection, selectedStyleName);
        }
        fireSelectionChanged();
    }

    private void buildTableFromModel() {
        int row = 0;
        resize(model.getRowSize(), 1);
        for (T next : this.model.getData()) {
            this.setWidget(row, 0, createWidgetForRow(next));

            // alternate table row colors
            getRowFormatter().setStyleName(row, "ted-table-row");
            if (row % 2 == 0) {
                getRowFormatter().setStyleName(row, "ted-table-row-alternate");
            }

            row++;
        }
    }

    protected abstract Widget createWidgetForRow(T modelObject);

    private void fireSelectionChanged() {
        for (TedTableSelectionListener listener : this.selectionListeners) {
            listener.selectionChanged();
        }
    }

}
