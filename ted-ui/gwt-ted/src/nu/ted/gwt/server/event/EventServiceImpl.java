package nu.ted.gwt.server.event;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TException;

import nu.ted.generated.Event;
import nu.ted.generated.TedService.Iface;
import nu.ted.gwt.client.event.EventService;
import nu.ted.gwt.domain.event.GwtEvent;
import nu.ted.gwt.server.page.search.TedRemoteServiceServlet;

public class EventServiceImpl extends TedRemoteServiceServlet implements EventService {

	private static final long serialVersionUID = 1L;

	@Override
	public List<GwtEvent> getEvents(String clientId) {
		List<Event> serverEvents;
		try {
			Iface client = getTedClient();
			serverEvents = client.getEvents();
			List<GwtEvent> events = new ArrayList<GwtEvent>();

			// Found events on the server, now convert them to GWT events.
			GwtEventFactory eventFactory = new GwtEventFactory();
			for(Event serverEvent : serverEvents) {
				events.add(eventFactory.createEvent(serverEvent));
			}
			return events;
		} catch (TException e) {
			// TODO: something better
			return new ArrayList<GwtEvent>();
		}
	}

	@Override
	public String registerForEvents() {
		try {
			Iface client = getTedClient();
			return client.registerClientWithEventRegistry();
		} catch (TException e) {
			// TODO: something better
			throw new RuntimeException("Unable to register", e);
		}
	}

}
