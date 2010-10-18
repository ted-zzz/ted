package nu.ted.gwt.server.page.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TException;

import net.bugsquat.diservlet.ImageStore;
import net.bugsquat.diservlet.StoredImage;
import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.InvalidOperation;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.generated.TedService.Iface;
import nu.ted.gwt.client.page.search.SearchService;
import nu.ted.gwt.domain.SearchSeriesInfo;
import nu.ted.gwt.domain.FoundSeries;


public class SearchServiceImpl extends TedRemoteServiceServlet implements
		SearchService {

	private static final long serialVersionUID = 1L;

	@Override
	public List<FoundSeries> search(final String filter) {
		try {
			Iface client = getTedClient();

			List<FoundSeries> foundSeries = new ArrayList<FoundSeries>();
			List<SeriesSearchResult> found;

			found = client.search(filter);
			for (SeriesSearchResult serie : found) {
				foundSeries.add(new FoundSeries(serie.getName(), serie.getSearchUID()));
			}

			return foundSeries;

			// TODO [MS] Should we filter watched shows out of the list?
		} catch (TException e) {
			// TODO: something better
			return new ArrayList<FoundSeries>();
		}
	}

	private String getOverview(final String searchUID) {

		String overview = "";
		try {
			Iface client = getTedClient();
			overview = client.getOverview(searchUID);
		} catch (TException e) {
			// TODO: something else?
		}
		return overview;
	}

	private boolean loadBanner(final String guideId, final ImageStore store) {

		try {
			Iface client = getTedClient();
			ImageFile imageFile = client.getImageByGuideId(guideId, ImageType.BANNER);
			if (imageFile.getData() == null || imageFile.getData().length == 0) {
				return false;
			}

			String mimeType = imageFile.getMimetype();
			if (mimeType == null || mimeType.isEmpty() || !mimeType.contains("image")) {
				return false;
			}

			StoredImage image = new StoredImage(mimeType, imageFile.getData());
			store.storeImage(guideId, image);
			return true;
		} catch (TException e) {
			// TODO: something?
		}
		return false;
	}

	@Override
	public SearchSeriesInfo getSeriesInfo(final String searchUUID) {
		String overview = getOverview(searchUUID);

		ImageStore store = getImageStore();
		if (store == null) {
			// Allow the client to continue without showing an image.
			return new SearchSeriesInfo(searchUUID, overview, false);
		}

		if (store.contains(searchUUID)) {
			return new SearchSeriesInfo(searchUUID, overview, true);
		}

		if (loadBanner(searchUUID, store)) {
			return new SearchSeriesInfo(searchUUID, overview, false);
		} else {
			return new SearchSeriesInfo(searchUUID, overview, true);
		}
	}

	@Override
	public void addWatchedSeries(String searchId) {
		try {
			Iface client = getTedClient();
			client.startWatching(searchId);
			// TODO [MS] Handle case where an exception is thrown.
		} catch (TException e) {
			// TODO: something?
		}
		catch (InvalidOperation e) {
			// TODO: something?

		}
	}

}
