package nu.ted.guide.tvdb.datasource;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.FSTORE;

import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.guide.DataSourceException;
import nu.ted.guide.tvdb.FullSeriesRecord;

public class CacheDataSourceTest {


	/* Simple service to return the 'first' only once.
	 * Then it should always return the 'rest'. It'll be used to test
	 * that the cache stores the first result and doesn't see the rest
	 */
	private static class WrappedService implements DataSource {

		FullSeriesRecord first;
		FullSeriesRecord rest;
		Boolean firstRun;

		public WrappedService(FullSeriesRecord first, FullSeriesRecord rest) {
			this.first = first;
			this.rest = rest;
			this.firstRun = true;
		}

		@Override
		public FullSeriesRecord getFullSeriesRecord(String id)
				throws DataSourceException {
			if (firstRun) {
				firstRun = false;
				return first;
			} else {
				return rest;
			}
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
		// Note, doing instance testing here, but it's easier than
		// generating the objects;
		FullSeriesRecord one = new FullSeriesRecord();
		FullSeriesRecord two = new FullSeriesRecord();
		CacheDataSource cache = new CacheDataSource(new WrappedService(one, two));

		FullSeriesRecord result;
		result = cache.getFullSeriesRecord("doesn't matter");
		assertSame(one, result);

		result = cache.getFullSeriesRecord("doesn't matter");
		assertSame(one, result);
	}
}
