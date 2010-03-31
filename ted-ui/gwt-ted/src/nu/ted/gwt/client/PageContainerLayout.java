package nu.ted.gwt.client;

import net.bugsquat.gwtsite.client.PageLoader;
import net.bugsquat.gwtsite.client.layout.DefaultPageContainerLayout;

import nu.ted.gwt.client.i18n.TedMessages;
import nu.ted.gwt.client.image.Images;
import nu.ted.gwt.client.Css.Application;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class PageContainerLayout extends DefaultPageContainerLayout {

	public PageContainerLayout()
	{
		getContent().setStyleName(Application.TED_CONTENT);
	}

	@Override
	protected FlowPanel createHeader() {
		FlowPanel header = new FlowPanel();
		header.setStyleName(Application.TED_HEADER);
		Image headerImage = new Image(Images.INSTANCE.headerLogo());
		headerImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PageLoader.getInstance().loadPage(TedPageId.WATCHED_SERIES);
			}
		});
		header.add(headerImage);

		Label title = new Label(TedMessages.INSTANCE.headerTitle());
		title.setStyleName(Application.TED_HEADER_TITLE);
		header.add(title);
		return header;
	}

}
