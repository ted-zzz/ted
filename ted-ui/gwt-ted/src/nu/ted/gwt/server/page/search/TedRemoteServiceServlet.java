package nu.ted.gwt.server.page.search;

import net.bugsquat.diservlet.ImageServlet;
import net.bugsquat.diservlet.ImageStore;
import nu.ted.client.JavaClient;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TedRemoteServiceServlet extends RemoteServiceServlet {

	protected JavaClient getTedClient() {
		return new JavaClient("localhost", 9030);
	}

	protected ImageStore getImageStore() {
		return (ImageStore) getServletContext().getAttribute(ImageServlet.IMAGE_STORE_ATTR_KEY);
	}

}
