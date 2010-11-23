package nu.ted.www;

import java.io.InputStream;

import nu.ted.DataTransferException;
import nu.ted.generated.ImageFile;

public interface PageLoader {
	
	InputStream openStream(String location) throws DataTransferException;

	ImageFile createImageFile(String location) throws DataTransferException;

}
