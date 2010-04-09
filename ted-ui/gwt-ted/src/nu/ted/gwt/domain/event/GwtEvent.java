package nu.ted.gwt.domain.event;

import java.io.Serializable;

public abstract class GwtEvent implements Serializable {

	private GwtEventType type;

	// Required by GWT.
	protected GwtEvent(){}

	public GwtEvent(GwtEventType type) {
		this.type = type;
	}

	public GwtEventType getType() {
		return type;
	}

	public void setType(GwtEventType type) {
		this.type = type;
	}

}
