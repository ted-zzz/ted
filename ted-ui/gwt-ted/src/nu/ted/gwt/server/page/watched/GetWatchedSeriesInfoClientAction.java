package nu.ted.gwt.server.page.watched;

import net.bugsquat.diservlet.ImageStore;
import nu.ted.generated.Series;
import nu.ted.generated.TedService.Client;
import nu.ted.gwt.domain.GwtWatchedSeries;

import org.apache.thrift.TException;

public class GetWatchedSeriesInfoClientAction extends WatchedSeriesClientAction {

	private GwtWatchedSeries series;
	private final Short seriesUID;

	public GetWatchedSeriesInfoClientAction(Short seriesUID, ImageStore imageStore) {
		super(imageStore);
		this.seriesUID = seriesUID;
	}

	@Override
	public void run(Client client) throws TException {
		Series watched = client.getSeries(seriesUID);
		if (watched == null) {
			// May have been removed by another user.
			return;
		}
		series = createNewWatchedSeries(client, watched);
	}

	public GwtWatchedSeries getSeries() {
		return series;
	}
}
