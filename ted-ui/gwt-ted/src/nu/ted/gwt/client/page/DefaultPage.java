package nu.ted.gwt.client.page;

import net.bugsquat.gwtsite.client.page.Page;
import net.bugsquat.gwtsite.client.page.PageController;
import nu.ted.gwt.client.Css.Application;

import com.google.gwt.user.client.ui.Widget;

/**
 * A basic page made up of a header and content, stacked vertically.
 *
 */
public abstract class DefaultPage<C extends PageController> extends Page<C> {

	public DefaultPage() {
		content.setStyleName(Application.TED_CONTENT);
		content.add(createPageHeader());
	}

	private Widget createPageHeader() {
		PageHeader header = new PageHeader();
		header.setHeaderText(getHeaderText());
		return header;
	}

	/**
	 * Defines the header text for this page.
	 *
	 * @return the header text.
	 */
	public abstract String getHeaderText();

}
