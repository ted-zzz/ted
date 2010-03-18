package nu.ted.gwt.client.image;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Images extends ClientBundle {

	public static Images INSTANCE = GWT.create(Images.class);
	
    /**
     * @return The header image.
     */
    @Source("header_logo.png")
    ImageResource headerLogo();

}
