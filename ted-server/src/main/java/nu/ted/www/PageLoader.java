package nu.ted.www;

import java.io.InputStream;

import nu.ted.generated.ImageFile;
import nu.ted.guide.DataTransferException;

public interface PageLoader {
	
	InputStream openStream(String location) throws DataTransferException;

	ImageFile createImageFile(String location) throws DataTransferException;

}
