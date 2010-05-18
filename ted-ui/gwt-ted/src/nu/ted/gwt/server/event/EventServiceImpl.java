package nu.ted.gwt.server.event;

import java.util.List;

import nu.ted.client.JavaClient;
import nu.ted.gwt.client.event.EventService;
import nu.ted.gwt.domain.event.GwtEvent;
import nu.ted.gwt.server.page.search.TedRemoteServiceServlet;

public class EventServiceImpl extends TedRemoteServiceServlet implements EventService {

	@Override
	public List<GwtEvent> getEvents(String clientId) {
		GetEventsClientAction action = new GetEventsClientAction(clientId);
		JavaClient ted = getTedClient();
		ted.run(action);
		return action.getEvents();
	}

	@Override
	public String registerForEvents() {
		RegisterClientAcion action = new RegisterClientAcion();
		JavaClient ted = getTedClient();
		ted.run(action);
		return action.getClientId();
	}

}
