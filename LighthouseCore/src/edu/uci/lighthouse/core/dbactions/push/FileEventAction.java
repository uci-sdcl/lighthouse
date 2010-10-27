package edu.uci.lighthouse.core.dbactions.push;

import java.util.Collection;

import edu.uci.lighthouse.core.controller.PushModel;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.jpa.JPAException;

public class FileEventAction extends AbstractEventAction {

	private static final long serialVersionUID = -8870330009338784335L;

	public FileEventAction(Collection<LighthouseEvent> events) {
		super(events);
	}

	@Override
	public void run() throws JPAException {
		PushModel.getInstance().saveEventsInDatabase(getEvents());
	}

}
