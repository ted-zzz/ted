package nu.ted.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

import net.bugsquat.gwtsite.client.layout.DefaultPageContainerLayout;
import nu.ted.gwt.client.i18n.TedMessages;
import nu.ted.gwt.client.image.Images;

public class PageContainerLayout extends DefaultPageContainerLayout {

    private static final String HEADER_STYLE = "ted-header";
    private static final String HEADER_TITLE_STYLE = "ted-header-title";
    
    public PageContainerLayout()
    {
    	getContent().setStyleName("ted-content");
    }
    
	@Override
	protected FlowPanel createHeader() {
		FlowPanel header = new FlowPanel();
        header.setStyleName(HEADER_STYLE);
        header.add(new Image(Images.INSTANCE.headerLogo()));

        Label title = new Label(TedMessages.INSTANCE.headerTitle());
        title.setStyleName(HEADER_TITLE_STYLE);
        header.add(title);
        return header;
	}

}
