package nu.ted.gwt.server.page.watched;

import java.util.ArrayList;
import java.util.List;

import net.bugsquat.diservlet.ImageStore;
import net.bugsquat.diservlet.StoredImage;
import nu.ted.client.ClientAction;
import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.Series;
import nu.ted.generated.TedService.Client;
import nu.ted.gwt.domain.GwtWatchedSeries;

import org.apache.thrift.TException;

public class GetWatchedSeriesClientAction extends WatchedSeriesClientAction {

	private List<GwtWatchedSeries> watched = new ArrayList<GwtWatchedSeries>();

	public GetWatchedSeriesClientAction(ImageStore imageStore) {
		super(imageStore);
	}

	@Override
	public void run(Client client) throws TException {
		watched.clear();

		List<Series> series = client.getWatching();
		for (Series serie : series) {
			watched.add(createNewWatchedSeries(client, serie));
		}
	}

	public List<GwtWatchedSeries> getWatched() {
		return watched;
	}

}
