package nu.ted.gwt.client.event;

import java.util.List;

import nu.ted.gwt.domain.GwtEvent;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("events")
public interface EventService extends RemoteService {

	String registerForEvents();

	List<GwtEvent> getEvents(String clientId);

}
