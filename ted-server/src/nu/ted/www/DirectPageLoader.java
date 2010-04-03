package nu.ted.www;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import nu.ted.generated.ImageFile;
import nu.ted.guide.DataTransferException;

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
			return new ImageFile(mimetype, out.toByteArray());
		} catch (IOException e) {
			throw new DataTransferException("Banner d/l failed", e);
		} finally {
			try { iStream.close(); } catch(IOException e) {}
			try { out.close(); } catch (IOException e) {}
		}
	}

}
