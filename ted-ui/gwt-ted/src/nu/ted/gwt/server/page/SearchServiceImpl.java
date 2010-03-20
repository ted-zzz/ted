package nu.ted.gwt.server.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TException;

import nu.ted.client.ClientAction;
import nu.ted.client.JavaClient;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.generated.TedService.Client;
import nu.ted.gwt.client.page.SearchService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SearchServiceImpl extends RemoteServiceServlet implements
		SearchService {

	@Override
	public List<String> search(final String filter) {
		JavaClient tedClient = new JavaClient("localhost", 9030);
		final ArrayList<String> shows = new ArrayList<String>();
		tedClient.run(new ClientAction() {
			
			@Override
			public void run(Client client) throws TException {
				List<SeriesSearchResult> found = client.search(filter);
				for (SeriesSearchResult serie : found) {
					shows.add("(" + serie.getSearchUID() + ") " + serie.getName());
				}
			}
		});
		return shows;
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	
}
