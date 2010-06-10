package nu.ted.guide.tvdb.datasource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;

import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.guide.DataSourceException;
import nu.ted.guide.tvdb.FullSeriesRecord;

public class CacheDataSource implements DataSource {

	private DataSource source;
	private Map<String, FullSeriesRecord> recordCache;

	//FullSeriesRecord cachedRecord;

	public CacheDataSource(DataSource source) {
		this.source = source;
		this.recordCache = new HashMap<String, FullSeriesRecord>();
	}

	@Override
	public FullSeriesRecord getFullSeriesRecord(String id)
			throws DataSourceException {
		if (!recordCache.containsKey(id)) {
			recordCache.put(id, source.getFullSeriesRecord(id));
		}
		return recordCache.get(id);
	}

	@Override
	public ImageFile getImage(String guideId, ImageType type)
			throws DataSourceException {
		throw new NotImplementedException();
	}

	@Override
	public List<SeriesSearchResult> search(String name)
			throws DataSourceException {
		throw new NotImplementedException();
	}

}
