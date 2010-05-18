package nu.ted.gwt.server.event;

import java.util.ArrayList;
import java.util.List;

import nu.ted.client.ClientAction;
import nu.ted.generated.Event;
import nu.ted.generated.TedService.Client;
import nu.ted.gwt.domain.event.GwtEvent;

import org.apache.thrift.TException;

public class GetEventsClientAction implements ClientAction {

	private final String clientId;
	private List<GwtEvent> events;

	public GetEventsClientAction(String clientId) {
		this.clientId = clientId;
		this.events = new ArrayList<GwtEvent>();
	}

	@Override
	public void run(Client client) throws TException {
		List<Event> serverEvents = client.getEvents(clientId);

		// Found events on the server, now convert them to GWT events.
		GwtEventFactory eventFactory = new GwtEventFactory();
		for(Event serverEvent : serverEvents) {
			events.add(eventFactory.createEvent(serverEvent));
		}
	}

	public List<GwtEvent> getEvents() {
		return events;
	}

}
