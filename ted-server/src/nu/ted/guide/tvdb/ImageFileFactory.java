package nu.ted.guide.tvdb;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import nu.ted.generated.ImageFile;
import nu.ted.generated.ImageType;
import nu.ted.guide.tvdb.Mirrors.NoMirrorException;

public class ImageFileFactory {

	private final Mirrors mirrors;

	public ImageFileFactory(Mirrors mirrors) {
		this.mirrors = mirrors;
	}

	public ImageFile createImage(FullSeriesRecord record, ImageType type)
		throws ImageFileFactoryException, ImageNotAvailableException {
		String location = getImageLocation(record, type);
		return createImageFile(location);
	}

	protected ImageFile createImageFile(String location) throws ImageFileFactoryException {
		InputStream iStream = null;
		ByteArrayOutputStream out = null;
		try {
			URL bannerURL = new URL(location);
			URLConnection bannerURLConnection = bannerURL.openConnection();
			String mimetype = bannerURLConnection.getContentType();

			iStream = new BufferedInputStream(bannerURLConnection.getInputStream());
			out = new ByteArrayOutputStream();

			byte[] buf = new byte[1024];
			int n = 0;
			while ((n = iStream.read(buf)) != -1) {
				out.write(buf, 0, n);
			}
			return new ImageFile(mimetype, out.toByteArray());
		}
		catch (MalformedURLException e) {
			throw new ImageFileFactoryException(e);
		} catch (IOException e) {
			throw new ImageFileFactoryException(e);
		}
		finally {
			if (iStream != null) try {iStream.close();} catch (IOException e) {};
			if (out != null) try {out.close();} catch (IOException e) {};
		}
	}

	private String getImageLocation(FullSeriesRecord record, ImageType type)
		throws ImageFileFactoryException, ImageNotAvailableException {
		if (type == ImageType.BANNER) {
			return buildBannerBaseUrl() + getBannerLocation(record);
		}
		else if (type == ImageType.BANNER_THUMBNAIL) {
			return buildBannerBaseUrl() + "_cache/" + getBannerLocation(record);
		}
		throw new RuntimeException("Image type not found.");
	}

	private String getBannerLocation(FullSeriesRecord record)
		throws ImageNotAvailableException {
		return checkImage(record.getBanner());
	}

	private String checkImage(String banner) throws ImageNotAvailableException {
		if (banner == null || banner.isEmpty()) {
			throw new ImageNotAvailableException();
		}
		return banner;
	}

	private String buildBannerBaseUrl() throws ImageFileFactoryException {
		try {
			return mirrors.getBannerMirror() + "/banners/";
		} catch (NoMirrorException e) {
			throw new ImageFileFactoryException(e);
		}
	}
}
