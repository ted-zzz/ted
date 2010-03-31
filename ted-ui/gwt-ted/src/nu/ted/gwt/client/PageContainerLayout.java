package nu.ted.gwt.client;

import net.bugsquat.gwtsite.client.PageLoader;
import net.bugsquat.gwtsite.client.layout.DefaultPageContainerLayout;

import nu.ted.gwt.client.i18n.TedMessages;
import nu.ted.gwt.client.image.Images;
import nu.ted.gwt.client.Css.Application;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

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
		headerImage.setStyleName(Css.Application.TED_HEADER_LOGO);
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
		header.add(createMenu());
		return header;
	}

	private FlowPanel createMenu() {
		// TODO [MS] Should create a menu widget at some point.
		FlowPanel menu = new FlowPanel();
		menu.setStyleName(Css.Application.TED_MENU);
		menu.add(createMenuItem("Search", TedPageId.SEARCH));
		menu.add(createSpacer());
		menu.add(createMenuItem("Watching", TedPageId.WATCHED_SERIES));

		return menu;
	}

	private Widget createSpacer() {
		Label spacer = new Label("|");
		spacer.setStyleName(Css.Application.TED_MENU_SPACER);
		return spacer;
	}

	public Label createMenuItem(final String name, final TedPageId pageToNavigateTo) {
		final Label menuItem = new Label(name);
		menuItem.setStyleName(Css.Application.TED_MENU_ITEM);
		menuItem.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PageLoader.getInstance().loadPage(pageToNavigateTo);
			}
		});

		menuItem.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				menuItem.addStyleDependentName(Css.Application.TED_MENU_ITEM_OVER);
			}
		});

		menuItem.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				menuItem.removeStyleDependentName(Css.Application.TED_MENU_ITEM_OVER);
			}
		});

		return menuItem;
	}

}
