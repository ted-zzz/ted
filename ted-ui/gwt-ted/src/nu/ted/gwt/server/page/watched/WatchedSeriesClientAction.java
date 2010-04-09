package nu.ted.gwt.server.page.watched;

import org.apache.thrift.TException;

import net.bugsquat.diservlet.ImageStore;
import net.bugsquat.diservlet.StoredImage;
import nu.ted.client.ClientAction;
import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.Series;
import nu.ted.generated.TedService.Client;
import nu.ted.gwt.domain.GwtWatchedSeries;

public abstract class WatchedSeriesClientAction implements ClientAction {

	private ImageStore imageStore;

	public WatchedSeriesClientAction(ImageStore imageStore) {
		this.imageStore = imageStore;
	}

	protected GwtWatchedSeries createNewWatchedSeries(Client ted, Series serie)
		throws TException {
		GwtWatchedSeries watched = new GwtWatchedSeries(serie.getUid(), serie.getName());

		String imageStoreKey = serie.getName() + serie.getUid();
		if (!imageStore.contains(imageStoreKey)) {

			ImageFile imageFile = ted.getImageBySeriesId(serie.getUid(), ImageType.BANNER_THUMBNAIL);
			if (imageFile.getData() == null || imageFile.getData().length == 0) {
				return watched;
			}

			String mimeType = imageFile.getMimetype();
			if (mimeType == null || mimeType.isEmpty() || !mimeType.contains("image")) {
				return watched;
			}

			StoredImage storedImage = new StoredImage(imageFile.getMimetype(), imageFile.getData());
			imageStore.storeImage(imageStoreKey, storedImage);
		}
		return watched;
	}

}
