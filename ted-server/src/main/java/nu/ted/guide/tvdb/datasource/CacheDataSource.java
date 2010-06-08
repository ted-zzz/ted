package nu.ted.guide.tvdb.datasource;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.guide.DataSourceException;
import nu.ted.guide.tvdb.FullSeriesRecord;

public class CacheDataSource implements DataSource {

	private DataSource source;
	private FullSeriesRecord cachedRecord;

	public CacheDataSource(DataSource source) {
		this.source = source;
	}

	@Override
	public FullSeriesRecord getFullSeriesRecord(String id)
			throws DataSourceException {
		if (cachedRecord == null) {
			cachedRecord = source.getFullSeriesRecord(id);
		}
		return cachedRecord;
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
