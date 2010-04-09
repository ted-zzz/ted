package nu.ted.gwt.client.event;

import java.util.List;

import nu.ted.gwt.domain.GwtEvent;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EventServiceAsync {

	void registerForEvents(AsyncCallback<String> callback);

	void getEvents(String clientId, AsyncCallback<List<GwtEvent>> callback);

}
