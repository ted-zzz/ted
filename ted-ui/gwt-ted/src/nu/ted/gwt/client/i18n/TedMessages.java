package nu.ted.gwt.client.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

/**
 * Defines all user facing text in the Ted web UI.
 *
 * @author mstead
 *
 */
public interface TedMessages extends Messages {
	public static TedMessages INSTANCE = GWT.create(TedMessages.class);
	
    String headerTitle();
}
