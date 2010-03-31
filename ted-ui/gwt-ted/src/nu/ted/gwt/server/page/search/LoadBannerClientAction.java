package nu.ted.gwt.server.page.search;

import org.apache.thrift.TException;

import net.bugsquat.diservlet.ImageStore;
import net.bugsquat.diservlet.StoredImage;
import nu.ted.client.ClientAction;
import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.TedService.Client;

public class LoadBannerClientAction implements ClientAction {

	private boolean imageAdded = false;
	private ImageStore imageStore;
	private String guideId;
	private String mimeType;

	public LoadBannerClientAction(String guideId, ImageStore imageStore) {
		this.guideId = guideId;
		this.imageStore = imageStore;
	}

	@Override
	public void run(Client client) throws TException {
		ImageFile imageFile = client.getImageByGuideId(guideId, ImageType.BANNER);
		if (imageFile.getData() == null || imageFile.getData().length == 0) {
			return;
		}

		mimeType = imageFile.getMimetype();
		if (mimeType == null || mimeType.isEmpty() || !mimeType.contains("image")) {
			return;
		}

		StoredImage image = new StoredImage(mimeType, imageFile.getData());
		imageStore.storeImage(guideId, image);
		imageAdded = true;
	}

	public boolean imageAdded() {
		return imageAdded;
	}

}
