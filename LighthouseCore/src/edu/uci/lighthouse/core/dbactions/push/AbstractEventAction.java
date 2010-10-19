package edu.uci.lighthouse.core.dbactions.push;

import java.util.Collection;

import edu.uci.lighthouse.core.dbactions.IDatabaseAction;
import edu.uci.lighthouse.model.LighthouseEvent;

public abstract class AbstractEventAction implements IDatabaseAction {

	private Collection<LighthouseEvent> events;

	public AbstractEventAction(Collection<LighthouseEvent> events) {
		this.events = events;
	}

	public Collection<LighthouseEvent> getEvents() {
		return events;
	}
}
