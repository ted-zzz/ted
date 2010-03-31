package nu.ted.guide.tvdb;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import nu.ted.domain.TestSeriesXml;
import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.guide.tvdb.Mirrors.Mask;

import org.junit.Before;
import org.junit.Test;

public class ImageFileFactoryTest {

	private TestImageFileFactory factory;
	private String expectedBannerMirror;

	@Before
	public void initTests() throws Exception {
		TestMirrorXml mirrorXml = new TestMirrorXml();
		mirrorXml.addMirror("banner_url", Mask.banner.value);
		Mirrors mirrors = Mirrors.createMirrors(mirrorXml.toStream());

		expectedBannerMirror = mirrors.getBannerMirror();
		factory = new TestImageFileFactory(mirrors);
	}

	@Test
	public void ensureCreateBanner() throws Exception {
		FullSeriesRecord record = FullSeriesRecord.create(new TestSeriesXml().toStream());
		String expectedLocation = expectedBannerMirror + "/banners/" + record.getBanner();

		ImageFile image = factory.createImage(record, ImageType.BANNER);
		assertEquals(expectedLocation, factory.location);
		assertNotNull(image);
		assertEquals("image/" + expectedLocation, image.getMimetype());
		assertArrayEquals(expectedLocation.getBytes(), image.getData());
	}

	@Test
	public void ensureCreateBannerThumbnail() throws Exception {
		FullSeriesRecord record = FullSeriesRecord.create(new TestSeriesXml().toStream());
		String expectedLocation = expectedBannerMirror + "/banners/_cache/" + record.getBanner();

		ImageFile image = factory.createImage(record, ImageType.BANNER_THUMBNAIL);
		assertEquals(expectedLocation, factory.location);
		assertNotNull(image);
		assertEquals("image/" + expectedLocation, image.getMimetype());
		assertArrayEquals(expectedLocation.getBytes(), image.getData());
	}

	@Test(expected = ImageFileFactoryException.class)
	public void ensureNoMirrorThrowsImageFileFactoryExceptionCreatingBanner() throws Exception {
		Mirrors mirrors = Mirrors.createMirrors(new TestMirrorXml().toStream());
		ImageFileFactory f = new ImageFileFactory(mirrors);
		f.createImage(null, ImageType.BANNER);
	}

	@Test(expected = ImageFileFactoryException.class)
	public void ensureNoMirrorThrowsImageFileFactoryExceptionCreatingThumbnail() throws Exception {
		Mirrors mirrors = Mirrors.createMirrors(new TestMirrorXml().toStream());
		ImageFileFactory f = new ImageFileFactory(mirrors);
		f.createImage(null, ImageType.BANNER_THUMBNAIL);
	}

	@Test(expected = ImageNotAvailableException.class)
	public void ensureImageNotAvailableIfBannerNotDefinedBySeries() throws Exception {
		TestSeriesXml seriesXml = new TestSeriesXml("");
		FullSeriesRecord record = FullSeriesRecord.create(seriesXml.toStream());
		factory.createImage(record, ImageType.BANNER);
	}

	private class TestImageFileFactory extends ImageFileFactory {
		private String location;

		public TestImageFileFactory(Mirrors mirrors) {
			super(mirrors);
		}

		@Override
		protected ImageFile createImageFile(String location)
				throws ImageFileFactoryException {
			this.location = location;
			return new ImageFile("image/" + location, location.getBytes());
		}

	}

}
