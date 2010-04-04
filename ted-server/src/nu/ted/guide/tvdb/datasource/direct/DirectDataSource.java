package nu.ted.guide.tvdb.datasource.direct;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.guide.DataSourceException;
import nu.ted.guide.DataTransferException;
import nu.ted.guide.DataUnavailableException;
import nu.ted.guide.tvdb.FullSeriesRecord;
import nu.ted.guide.tvdb.SearchResults;
import nu.ted.guide.tvdb.SearchResults.TVDBSeries;
import nu.ted.guide.tvdb.datasource.DataSource;
import nu.ted.guide.tvdb.datasource.direct.Mirrors.NoMirrorException;
import nu.ted.www.PageLoader;

public class DirectDataSource implements DataSource {
	public static final String APIKEY = "0D513FDFA9D09C21";

	private Mirrors mirrors;
	private PageLoader loader;

	public DirectDataSource(PageLoader loader) throws DataTransferException {
		this.loader = loader;
		try {
			this.mirrors = Mirrors.createMirrors(loader.openStream("http://www.thetvdb.com/api/" + APIKEY
					+ "/mirrors.xml"));
		} catch (NoMirrorException e) {
			throw new DataTransferException(e);
		} catch (DataSourceException e) {
			throw new DataTransferException(e);
		}
		
	}

	DirectDataSource(Mirrors mirrors) {
		this.mirrors = mirrors;
	}

	// This is slow, which is why the Direct Data Source should be wrapped.
	@Override
	public FullSeriesRecord getFullSeriesRecord(String guideId)
			throws DataSourceException {

		// TODO: look at what if you guideID is invalid
		// we should be sending a DataUnavailableException in that case.
		try {
			return FullSeriesRecord.create(loader.openStream(mirrors.getXMLMirror() + "/api/" + APIKEY + "/series/"
					+ guideId + "/all/"));
		} catch (NoMirrorException e) {
			throw new DataTransferException(e);
		}
	}

	@Override
	public List<SeriesSearchResult> search(String name) throws DataSourceException
	{
		String nameEncoded;
		String location;
		List<SeriesSearchResult> returner = new LinkedList<SeriesSearchResult>();
		try {
			nameEncoded = URLEncoder.encode(name, "UTF-8");
			location = mirrors.getXMLMirror() + "/api/GetSeries.php?seriesname=" + nameEncoded;

			SearchResults searchResults =  SearchResults.create(loader.openStream(location));
			for (TVDBSeries result : searchResults.getSeriesList()) {
				// TODO: TVDBSeries <--> Series conversion should be moved somewhere.
				SeriesSearchResult s = new SeriesSearchResult(result.getId(), result.getName());
				returner.add(s);
			}
		} catch (NoMirrorException e) {
			throw new DataTransferException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		return returner;
	}

	@Override
	public ImageFile getImage(String guideId, ImageType type)
			throws DataSourceException {
		String location = getImageLocation(guideId, type);
		return loader.createImageFile(location);
	}

	private String getImageLocation(String guideId, ImageType type)
			throws DataSourceException {

		String base;
		try {
			base = mirrors.getBannerMirror() + "/banners/";
		} catch (NoMirrorException e) {
			throw new DataTransferException(e);
		}

		if (type == ImageType.BANNER) {
			return base + getBannerLocation(guideId);
		} else if (type == ImageType.BANNER_THUMBNAIL) {
			return base + "_cache/" + getBannerLocation(guideId);
		}
		throw new RuntimeException("Image type not found.");
	}

	private String getBannerLocation(String guideId)
			throws DataSourceException {
		FullSeriesRecord record = getFullSeriesRecord(guideId);
		String banner = record.getBanner();
		if (banner == null || banner.isEmpty()) {
			throw new DataUnavailableException("No banner on record.");
		}
		return banner;
	}
}
