package nu.ted.www;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;

import nu.ted.DataTransferException;
import nu.ted.generated.ImageFile;
import nu.ted.image.ImageLoader;

public class DirectPageLoader implements PageLoader {

	@Override
	public InputStream openStream(String location) throws DataTransferException {

		URL url;
		try {
			url = new URL(location);
			URLConnection connection = url.openConnection();
			// TODO: set connection timeout

			return connection.getInputStream();
		} catch (MalformedURLException e) {
			throw new DataTransferException(e);
		} catch (IOException e) {
			throw new DataTransferException(e);
		}
	}

	@Override
	public ImageFile createImageFile(String location) throws DataTransferException {
		return ImageLoader.createImageFile(location);
	}

}
