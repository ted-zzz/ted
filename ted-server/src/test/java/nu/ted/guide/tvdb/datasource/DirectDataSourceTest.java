package nu.ted.guide.tvdb.datasource;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import nu.ted.domain.TestSeriesXml;
import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.guide.DataTransferException;
import nu.ted.guide.DataUnavailableException;
import nu.ted.guide.tvdb.TestMirrorXml;
import nu.ted.guide.tvdb.datasource.DirectDataSource;
import nu.ted.guide.tvdb.datasource.Mirrors.Mask;
import nu.ted.www.PageLoader;

public class DirectDataSourceTest {

	TestMirrorXml mirrorXml;

	class TestPageLoader implements PageLoader {

		public Map<String, ImageFile> images;
		public Map<String, TestSeriesXml> series;

		public TestPageLoader() {
			images = new HashMap<String, ImageFile>();
			series = new HashMap<String, TestSeriesXml>();
		}

		public void addImage(String location, ImageFile file) {
			images.put(location, file);
		}

		public void addFullSeriesRecord(String location, TestSeriesXml seriesXML)
		{
			series.put(location, seriesXML);
		}

		@Override
		public InputStream openStream(String location) throws DataTransferException {

			if (location.endsWith("mirrors.xml")) {
				return mirrorXml.toStream();
			}

			if (series.containsKey(location)) {
				return series.get(location).toStream();
			}

			throw new DataTransferException("Unknown location - Testing error");
		}

		@Override
		public ImageFile createImageFile(String location)
				throws DataTransferException {

			if (images.containsKey(location)) {
				return images.get(location);
			}
			throw new DataTransferException("Test Error - Unknown image.");
		}
	};

	// TODO: the actual URLs should be extracted so we don't hardcode them in the tests.
	@Test
	public void ensureCreateBanner() throws Exception {
		mirrorXml = new TestMirrorXml();
		mirrorXml.addMirror("banner_url", Mask.banner.value);
		mirrorXml.addMirror("test-url", Mask.xml.value);

		TestPageLoader loader = new TestPageLoader();

		// Add Series - currently hardcoded to series name 1000
		TestSeriesXml testXml = new TestSeriesXml();
		loader.addFullSeriesRecord("test-url/api/" + DirectDataSource.APIKEY + "/series/1000/all/", testXml);

		// Add Banner
		ImageFile testImage = new ImageFile("image/test", ByteBuffer.wrap("abcd".getBytes()));
		loader.addImage("banner_url/banners/graphical/73244-g9.jpg", testImage);

		DirectDataSource dds = new DirectDataSource(loader);
		ImageFile image = dds.getImage("1000", ImageType.BANNER);

		assertNotNull(image);
		assertEquals("image/test", image.getMimetype());
		assertArrayEquals("abcd".getBytes(), image.getData());

	}

	@Test
	public void ensureCreateBannerThumbnail() throws Exception {
		mirrorXml = new TestMirrorXml();
		mirrorXml.addMirror("banner_url", Mask.banner.value);
		mirrorXml.addMirror("test-url", Mask.xml.value);

		TestPageLoader loader = new TestPageLoader();

		// Add Series - currently hardcoded to series name 1000
		TestSeriesXml testXml = new TestSeriesXml();
		loader.addFullSeriesRecord("test-url/api/" + DirectDataSource.APIKEY + "/series/1000/all/", testXml);

		// Add Banner
		ImageFile testImage = new ImageFile("image/test", ByteBuffer.wrap("abcd".getBytes()));
		loader.addImage("banner_url/banners/_cache/graphical/73244-g9.jpg", testImage);

		DirectDataSource dds = new DirectDataSource(loader);
		ImageFile image = dds.getImage("1000", ImageType.BANNER_THUMBNAIL);

		assertNotNull(image);
		assertEquals("image/test", image.getMimetype());
		assertArrayEquals("abcd".getBytes(), image.getData());

	}

	@Test(expected = DataUnavailableException.class)
	public void ensureNoMirrorThrowsDataUnavailableException() throws Exception {
		mirrorXml = new TestMirrorXml();
		mirrorXml.addMirror("test-url", Mask.xml.value);
		// No Banner URL set

		TestPageLoader loader = new TestPageLoader();

		TestSeriesXml testXml = new TestSeriesXml();
		loader.addFullSeriesRecord("test-url/api/" + DirectDataSource.APIKEY + "/series/1000/all/", testXml);

		DirectDataSource dds = new DirectDataSource(loader);

		dds.getImage("1000", ImageType.BANNER);

	}

	@Test(expected = DataUnavailableException.class)
	public void ensureMissingBannerThrowsDataUnavailableException() throws Exception {
		mirrorXml = new TestMirrorXml();
		mirrorXml.addMirror("banner_url", Mask.banner.value);
		mirrorXml.addMirror("test-url", Mask.xml.value);

		TestPageLoader loader = new TestPageLoader();

		// Add Series - currently hardcoded to series name 1000
		TestSeriesXml testXml = new TestSeriesXml(""); // blank banner location
		loader.addFullSeriesRecord("test-url/api/" + DirectDataSource.APIKEY + "/series/1000/all/", testXml);

		DirectDataSource dds = new DirectDataSource(loader);
		dds.getImage("1000", ImageType.BANNER);

	}

	@Test(expected = DataUnavailableException.class)
	public void ensureMissingBannerThumbnailThrowsDataUnavailableException() throws Exception {
		mirrorXml = new TestMirrorXml();
		mirrorXml.addMirror("banner_url", Mask.banner.value);
		mirrorXml.addMirror("test-url", Mask.xml.value);

		TestPageLoader loader = new TestPageLoader();

		// Add Series - currently hardcoded to series name 1000
		TestSeriesXml testXml = new TestSeriesXml(""); // blank banner location
		loader.addFullSeriesRecord("test-url/api/" + DirectDataSource.APIKEY + "/series/1000/all/", testXml);

		DirectDataSource dds = new DirectDataSource(loader);
		dds.getImage("1000", ImageType.BANNER_THUMBNAIL);

	}
}
