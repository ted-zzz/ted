package nu.ted.image;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;

import org.apache.thrift.TException;

import nu.ted.DataTransferException;
import nu.ted.DataUnavailableException;
import nu.ted.generated.ImageFile;

public class ImageLoader {

	private static final String IMAGE_UNAVAILABLE_PNG = "ImageUnavailable.png";

	public static ImageFile createImageFile(String location) throws DataTransferException {
		InputStream iStream = null;
		ByteArrayOutputStream out = null;

		URL bannerURL;
		try {
			bannerURL = new URL(location);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Invalid Image URL " + location, e);
		}

		URLConnection bannerURLConnection;
		try {
			bannerURLConnection = bannerURL.openConnection();
		} catch (IOException e) {
			throw new DataTransferException("Unable to connect to banner url.", e);
		}

		out = new ByteArrayOutputStream();
		try {
			String mimetype = bannerURLConnection.getContentType();

			iStream = new BufferedInputStream(bannerURLConnection.getInputStream());

			byte[] buf = new byte[1024];
			int n = 0;
			while ((n = iStream.read(buf)) != -1) {
				out.write(buf, 0, n);
			}
			return new ImageFile(mimetype, ByteBuffer.wrap(out.toByteArray()));
		} catch (IOException e) {
			throw new DataTransferException("Banner d/l failed", e);
		} finally {
			try { iStream.close(); } catch(IOException e) {}
			try { out.close(); } catch (IOException e) {}
		}
	}

	public static ImageFile getImageUnavailableImage() throws DataUnavailableException {
		String unavailableMessage = "Could not load image: " + IMAGE_UNAVAILABLE_PNG;

		URL resource = ImageLoader.class.getResource(IMAGE_UNAVAILABLE_PNG);
		if (resource == null) {
			throw new DataUnavailableException(unavailableMessage);
		}

		try {
			return createImageFile(resource.toURI().toString());
		} catch (DataTransferException e) {
			throw new DataUnavailableException(unavailableMessage, e);
		} catch (URISyntaxException e) {
			throw new DataUnavailableException(unavailableMessage, e);
		}
	}

}
