package nu.ted.gwt.client.widget.table;

import java.util.ArrayList;
import java.util.List;


/**
 * Table model data for a {@link TedTable}.
 *
 * @author mstead
 *
 */
public class TedTableModel<T> {

    private List<T> data = new ArrayList<T>();
    private List<TedTableModelListener> listeners = new ArrayList<TedTableModelListener>();

    public int getRowSize() {
        return data.size();
    }

    public void setData(List<T> rowData) {
    	this.data = rowData;
        notifyModelChanged();
    }

    public T getSerie(int row) {
        if (data.size() < row)
            throw new RuntimeException("Model out of sync with table.");
        return data.get(row);
    }

    public List<T> getData() {
        return this.data;
    }

    public void clear() {
        data.clear();
        notifyDataCleared();
    }

    public void addModelListener(TedTableModelListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    private void notifyDataCleared() {
        for (TedTableModelListener listener : this.listeners) {
            listener.dataCleared();
        }
    }

    private void notifyModelChanged() {
        for (TedTableModelListener listener : this.listeners) {
            listener.modelChanged();
        }
    }
}
