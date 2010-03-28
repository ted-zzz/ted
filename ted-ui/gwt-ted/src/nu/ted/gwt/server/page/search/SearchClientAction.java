package nu.ted.gwt.server.page.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TException;

import nu.ted.client.ClientAction;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.generated.TedService.Client;
import nu.ted.gwt.domain.FoundSeries;

public class SearchClientAction implements ClientAction {

    private final String filter;
    private List<FoundSeries> foundSeries;

    public SearchClientAction(String filter) {
        this.filter = filter;
        this.foundSeries = new ArrayList<FoundSeries>();
    }

    @Override
    public void run(Client client) throws TException {
        // Make sure the results are emptied in case the action was
        // reused and run twice.
        getFoundSeries().clear();

        List<SeriesSearchResult> found = client.search(filter);
        for (SeriesSearchResult serie : found) {
            getFoundSeries().add(new FoundSeries(serie.getName(), serie.getSearchUID()));
        }
    }

    public List<FoundSeries> getFoundSeries() {
        return foundSeries;
    }

}
