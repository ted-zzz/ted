package nu.ted.gwt.client.page;

import nu.ted.gwt.client.Css.Application;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PageHeader extends Widget {

	private Element headerTextElement;

	public PageHeader() {

		Element content = DOM.createDiv();
		content.setClassName(Application.TED_PAGE_HEADER);

		headerTextElement = DOM.createDiv();
		headerTextElement.setClassName(Application.TED_PAGE_HEADER_TEXT);
		content.appendChild(headerTextElement);

		setElement(content);
	}

	public void setHeaderText(String headerText) {
		this.headerTextElement.setInnerText(headerText);
	}
}
