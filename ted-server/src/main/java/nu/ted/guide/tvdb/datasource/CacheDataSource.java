package nu.ted.guide.tvdb.datasource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nu.ted.DataRetrievalException;
import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.guide.tvdb.FullSeriesRecord;

public class CacheDataSource implements DataSource {

	private DataSource source;
	private Map<String, FullSeriesRecord> recordCache;
	private Map<String, ImageFile> bannerCache;
	private Map<String, ImageFile> bannerThumbCache;

	//FullSeriesRecord cachedRecord;

	public CacheDataSource(DataSource source) {
		this.source = source;
		this.recordCache = new HashMap<String, FullSeriesRecord>();
		this.bannerCache = new HashMap<String, ImageFile>();
		this.bannerThumbCache = new HashMap<String, ImageFile>();
	}

	@Override
	public FullSeriesRecord getFullSeriesRecord(String id)
			throws DataRetrievalException {
		if (!recordCache.containsKey(id)) {
			recordCache.put(id, source.getFullSeriesRecord(id));
		}
		return recordCache.get(id);
	}

	@Override
	public ImageFile getImage(String guideId, ImageType type)
			throws DataRetrievalException {
		Map<String, ImageFile> cache;
		if (type == ImageType.BANNER)
			cache = bannerCache;
		else if (type == ImageType.BANNER_THUMBNAIL)
			cache = bannerThumbCache;
		else
			throw new UnsupportedOperationException("Invalid Imagetype: " + type.toString());

		if (!cache.containsKey(guideId)) {
			cache.put(guideId, source.getImage(guideId, type));
		}
		return cache.get(guideId);
	}

	@Override
	public List<SeriesSearchResult> search(String name)
			throws DataRetrievalException {

		return source.search(name);
	}

	public void remove(String id) {
		recordCache.remove(id);
	}

}
