package nu.ted.guide.tvdb.datasource;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.Test;

import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.generated.SeriesSearchResult;
import nu.ted.guide.DataSourceException;
import nu.ted.guide.tvdb.FullSeriesRecord;

public class CacheDataSourceTest {


	private static class WrappedService implements DataSource {

		Queue<FullSeriesRecord> records;
		Queue<ImageFile> images;

		public WrappedService(List<FullSeriesRecord> records,
				List<ImageFile> images) {
			if (records != null)
				this.records = new LinkedList<FullSeriesRecord>(records);
			else
				this.records = null;

			if (images != null)
				this.images = new LinkedList<ImageFile>(images);
			else
				this.images = null;
		}

		@Override
		public FullSeriesRecord getFullSeriesRecord(String id)
				throws DataSourceException {
			return records.poll();
		}

		@Override
		public ImageFile getImage(String guideId, ImageType type)
				throws DataSourceException {
			return images.poll();
		}

		@Override
		public List<SeriesSearchResult> search(String name)
				throws DataSourceException {
			throw new UnsupportedOperationException();
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

		CacheDataSource cache = new CacheDataSource(new WrappedService(records, null));

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

		CacheDataSource cache = new CacheDataSource(new WrappedService(records, null));

		FullSeriesRecord result;
		result = cache.getFullSeriesRecord("A");
		assertSame(one, result);

		result = cache.getFullSeriesRecord("B");
		assertSame(two, result);
	}

	@Test
	public void shouldBeAbleToRemoveInvalidRecordsFromCache() throws DataSourceException {
		List<FullSeriesRecord> records = new LinkedList<FullSeriesRecord>();

		FullSeriesRecord one = new FullSeriesRecord();
		records.add(one);

		FullSeriesRecord two = new FullSeriesRecord();
		records.add(two);

		CacheDataSource cache = new CacheDataSource(new WrappedService(records, null));

		FullSeriesRecord result;
		result = cache.getFullSeriesRecord("A");

		cache.remove("A");

		result = cache.getFullSeriesRecord("A");
		assertSame(two, result);
	}

	@Test
	public void shouldReturnCachedImages() throws DataSourceException {
		List<ImageFile> images = new LinkedList<ImageFile>();
		images.add(new ImageFile("Num1", ByteBuffer.wrap("abc".getBytes())));
		images.add(new ImageFile("Num2", ByteBuffer.wrap("abc".getBytes())));

		CacheDataSource cache = new CacheDataSource(new WrappedService(null, images));
		ImageFile result;

		result = cache.getImage("id", ImageType.BANNER);
		assertEquals("Num1", result.getMimetype());

		result = cache.getImage("id", ImageType.BANNER);
		assertEquals("Num1", result.getMimetype());
	}

	@Test
	public void shouldReturnOnlyOnExactSameImage() throws DataSourceException {
		List<ImageFile> images = new LinkedList<ImageFile>();
		images.add(new ImageFile("Num1", ByteBuffer.wrap("abc".getBytes())));
		images.add(new ImageFile("Num2", ByteBuffer.wrap("abc".getBytes())));
		images.add(new ImageFile("Num3", ByteBuffer.wrap("abc".getBytes())));

		CacheDataSource cache = new CacheDataSource(new WrappedService(null, images));
		ImageFile result;

		result = cache.getImage("id", ImageType.BANNER);
		assertEquals("Num1", result.getMimetype());

		// Same key, but different type
		result = cache.getImage("id", ImageType.BANNER_THUMBNAIL);
		assertEquals("Num2", result.getMimetype());

		// Different key, same type
		result = cache.getImage("different id", ImageType.BANNER);
		assertEquals("Num3", result.getMimetype());
	}

	// Don't remove the images as they're not critical to operation.
	@Test
	public void removeShouldLeaveImagesAlone() throws DataSourceException {
		List<ImageFile> images = new LinkedList<ImageFile>();
		images.add(new ImageFile("Num1", ByteBuffer.wrap("abc".getBytes())));
		images.add(new ImageFile("Num2", ByteBuffer.wrap("abc".getBytes())));

		CacheDataSource cache = new CacheDataSource(new WrappedService(null, images));
		ImageFile result;

		result = cache.getImage("id", ImageType.BANNER);
		assertEquals("Num1", result.getMimetype());

		cache.remove("id");

		result = cache.getImage("id", ImageType.BANNER);
		assertEquals("Num1", result.getMimetype());

	}

	// Other tests: limit cache size (and keep recent at front) - searches
	// maybe a way to evaluate memory usage?
}
