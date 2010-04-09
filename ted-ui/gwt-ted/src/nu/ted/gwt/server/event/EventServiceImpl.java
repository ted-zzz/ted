package nu.ted.gwt.server.event;

import java.util.List;

import org.apache.thrift.TException;

import nu.ted.client.ClientAction;
import nu.ted.client.JavaClient;
import nu.ted.generated.TedService.Client;
import nu.ted.gwt.client.event.EventService;
import nu.ted.gwt.domain.GwtEvent;
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
		System.out.println("Client registered: " + action.getClientId());
		return action.getClientId();
	}

}
