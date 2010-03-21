package nu.ted.gwt.client.page;

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
        content.setClassName("ted-page-header");

        headerTextElement = DOM.createDiv();
        headerTextElement.setClassName("ted-page-header-text");
        content.appendChild(headerTextElement);

        setElement(content);
    }

    public void setHeaderText(String headerText) {
        this.headerTextElement.setInnerText(headerText);
    }
}
