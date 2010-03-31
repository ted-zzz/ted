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

public class GetWatchedSeriesClientAction implements ClientAction {
	private List<GwtWatchedSeries> watched = new ArrayList<GwtWatchedSeries>();
	private ImageStore imageStore;

	public GetWatchedSeriesClientAction(ImageStore imageStore) {
		this.imageStore = imageStore;
	}

	@Override
	public void run(Client client) throws TException {
		watched.clear();

		List<Series> series = client.getWatching();
		for (Series serie : series) {
			watched.add(new GwtWatchedSeries(serie.getUid(), serie.getName()));

			String imageStoreKey = serie.getName() + serie.getUid();
			if (imageStore.contains(imageStoreKey))
				continue;

			ImageFile imageFile = client.getImageBySeriesId(serie.getUid(), ImageType.BANNER_THUMBNAIL);
			if (imageFile.getData() == null || imageFile.getData().length == 0) {
				continue;
			}

			String mimeType = imageFile.getMimetype();
			if (mimeType == null || mimeType.isEmpty() || !mimeType.contains("image")) {
				continue;
			}

			StoredImage storedImage = new StoredImage(imageFile.getMimetype(), imageFile.getData());
			imageStore.storeImage(imageStoreKey, storedImage);
		}
	}

	public List<GwtWatchedSeries> getWatched() {
		return watched;
	}

}
