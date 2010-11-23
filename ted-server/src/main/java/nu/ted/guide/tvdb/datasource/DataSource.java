package nu.ted.guide.tvdb.datasource;

import java.util.List;

import nu.ted.DataSourceException;
import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.guide.tvdb.FullSeriesRecord;

// This is the interface for where TVDB will get it's results from:
public interface DataSource {

	FullSeriesRecord getFullSeriesRecord(String id) throws DataSourceException;

	ImageFile getImage(String guideId, ImageType type)
			throws DataSourceException;

	List<SeriesSearchResult> search(String name) throws DataSourceException;


}
