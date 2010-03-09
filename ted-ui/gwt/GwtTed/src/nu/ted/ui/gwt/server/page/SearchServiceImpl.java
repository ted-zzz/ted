package nu.ted.ui.gwt.server.page;

import java.util.ArrayList;
import java.util.List;

import nu.ted.ui.gwt.client.page.SearchService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SearchServiceImpl extends RemoteServiceServlet implements
		SearchService {

	@Override
	public List<String> search(String filter) {
		ArrayList<String> results = new ArrayList<String>();
		results.add("One");
		results.add("Two");
		results.add("Three");
		results.add("Four");
		results.add("Five");
		return results;
	}

}
