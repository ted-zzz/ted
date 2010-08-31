package nu.ted.gwt.server.page.watched;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TException;

import net.bugsquat.diservlet.StoredImage;
import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.Series;
import nu.ted.generated.TedService.Iface;
import nu.ted.gwt.client.page.watched.WatchedSeriesService;
import nu.ted.gwt.domain.GwtWatchedSeries;
import nu.ted.gwt.server.page.search.TedRemoteServiceServlet;

public class WatchedSeriesServiceImpl extends TedRemoteServiceServlet
	implements WatchedSeriesService {

	private static final long serialVersionUID = 1L;

	protected GwtWatchedSeries createNewWatchedSeries(Iface ted, Series series)
	throws TException {
		GwtWatchedSeries watched = GwtTedObjectFactory.create(series);

		String imageStoreKey = series.getName() + series.getUid();
		if (!getImageStore().contains(imageStoreKey)) {

			try {
				ImageFile imageFile = ted.getImageBySeriesId(series.getUid(), ImageType.BANNER_THUMBNAIL);

				if (imageFile.getData() == null || imageFile.getData().array().length == 0) {
					return watched;
				}

				String mimeType = imageFile.getMimetype();
				if (mimeType == null || mimeType.isEmpty() || !mimeType.contains("image")) {
					return watched;
				}

				StoredImage storedImage = new StoredImage(imageFile.getMimetype(), imageFile.getData().array());
				getImageStore().storeImage(imageStoreKey, storedImage);

			} catch (TException e) { // TODO: should probably be more specific as to not catch transport errors
				// Nothing, just ignore the missing image
			}
		}
		return watched;
	}

	@Override
	public List<GwtWatchedSeries> getWatchedSeries() {

		List<GwtWatchedSeries> watched = new ArrayList<GwtWatchedSeries>();
		try {
			Iface client = getTedClient();
			List<Series> series = client.getWatching();
			for (Series serie : series) {
				watched.add(createNewWatchedSeries(client, serie));
			}
		} catch (TException e) {
			// TODO: something
		}

		return watched;
	}

	@Override
	public void stopWatching(short seriesUid) {
		try {
			Iface client = getTedClient();
			client.stopWatching(seriesUid);
		} catch (TException e) {
			// TODO: something
		}

	}

	@Override
	public GwtWatchedSeries getWatchedSeriesInfo(short seriesUID) {

		Iface client;
		try {
			client = getTedClient();
			Series watched = client.getSeries(seriesUID);
			if (watched != null) {
				return createNewWatchedSeries(client, watched);
				// TODO: are we sure about returning null?
			}
			// May have been removed by another user.
		} catch (TException e) {
			// TODO: something?
		}
		return null;
	}


}
