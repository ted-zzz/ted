package nu.ted.ui.gwt.client.page;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SearchServiceAsync {

	void search(String filter, AsyncCallback<List<String>> callback);

}
