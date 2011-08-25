package edu.uci.lighthouse.lighthouseqandathreads;

import java.util.Collection;

import edu.uci.lighthouse.core.controller.PushModel;
import edu.uci.lighthouse.core.dbactions.push.AbstractEventAction;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.jpa.JPAException;

public class ForumAddEventAction extends AbstractEventAction{

	public ForumAddEventAction(Collection<LighthouseEvent> events) {
		super(events);
	}

	@Override
	public void run() throws JPAException {
		PushModel.getInstance().saveEventsInDatabase(getEvents());
		
	}

}
