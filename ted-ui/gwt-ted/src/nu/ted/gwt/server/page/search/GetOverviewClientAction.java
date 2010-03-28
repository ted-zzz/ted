package nu.ted.gwt.server.page.search;

import org.apache.thrift.TException;

import nu.ted.client.ClientAction;
import nu.ted.generated.TedService.Client;

public class GetOverviewClientAction implements ClientAction
{
	private final String searchUID;
	private String overview;

	public GetOverviewClientAction(String searchUID)
	{
		this.searchUID = searchUID;
		this.overview = "";
	}

	@Override
	public void run(Client client) throws TException {
		this.overview = client.getOverview(searchUID);
	}

	public String getOverview() {
		return this.overview;
	}

}
