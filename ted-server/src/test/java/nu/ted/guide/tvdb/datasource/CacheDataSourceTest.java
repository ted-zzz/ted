package nu.ted.guide.tvdb.datasource;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Test;

import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.guide.DataSourceException;
import nu.ted.guide.tvdb.FullSeriesRecord;

public class CacheDataSourceTest {


	private static class WrappedService implements DataSource {

		Queue<FullSeriesRecord> records;

		public WrappedService(List<FullSeriesRecord> records) {
			this.records = new LinkedList<FullSeriesRecord>(records);
		}

		@Override
		public FullSeriesRecord getFullSeriesRecord(String id)
				throws DataSourceException {
			return records.poll();
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

	@Test
	public void shouldAlwaysReturnFirst() throws DataSourceException {
		// Note: doing instance testing here, but it's easier than
		// generating the objects. There's no reason why the CDS
		// has to return the exact same object though.
		List<FullSeriesRecord> records = new LinkedList<FullSeriesRecord>();

		FullSeriesRecord one = new FullSeriesRecord();
		records.add(one);

		FullSeriesRecord two = new FullSeriesRecord();
		records.add(two);

		CacheDataSource cache = new CacheDataSource(new WrappedService(records));

		FullSeriesRecord result;
		result = cache.getFullSeriesRecord("doesn't matter");
		assertSame(one, result);

		result = cache.getFullSeriesRecord("doesn't matter");
		assertSame(one, result);
	}

	@Test
	public void shouldReturnCorrectRecordForId() throws DataSourceException {
		List<FullSeriesRecord> records = new LinkedList<FullSeriesRecord>();

		FullSeriesRecord one = new FullSeriesRecord();
		records.add(one);

		FullSeriesRecord two = new FullSeriesRecord();
		records.add(two);

		CacheDataSource cache = new CacheDataSource(new WrappedService(records));

		FullSeriesRecord result;
		result = cache.getFullSeriesRecord("A");
		assertSame(one, result);

		result = cache.getFullSeriesRecord("B");
		assertSame(two, result);
	}

	// Other tests: limit cache size (and keep recent at front) - images - searches
	// maybe a way to evaluate memory usage?
}
