package nu.ted.gwt.client.widget.table;

/**
 * Listens to selection events on the {@link TedTable}.
 * 
 * @author mstead
 *
 */
public interface TedTableSelectionListener {
	
	/**
	 * Called when the selected row is changed on the {@link TedTable}.
	 */
	public void selectionChanged();
}
