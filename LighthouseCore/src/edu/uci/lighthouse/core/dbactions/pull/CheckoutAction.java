package edu.uci.lighthouse.core.dbactions.pull;

import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import edu.uci.lighthouse.core.controller.Controller;
import edu.uci.lighthouse.core.controller.PullModel;
import edu.uci.lighthouse.core.controller.TimestampLastEventReceived;
import edu.uci.lighthouse.core.controller.WorkingCopy;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.jpa.JPAException;

public class CheckoutAction extends AbstractWorkingCopyAction {

	private static final long serialVersionUID = -435718370337302593L;

	private static Logger logger = Logger.getLogger(CheckoutAction.class);
	
	public CheckoutAction(WorkingCopy workingCopy) {
		super(workingCopy);
	}

	@Override
	public void run() throws JPAException {
		PullModel pullModel = new PullModel(LighthouseModel.getInstance());
		Collection<LighthouseEvent> events = pullModel.executeQueryCheckout(getWorkingCopy());
		LighthouseModel.getInstance().fireModelChanged();
		Date timestampLastEventReceived = Controller.getLatestTime(events);
		TimestampLastEventReceived.getInstance().setValue(timestampLastEventReceived);
		logger.info("Number of events fetched after checkout = " + events.size());
	}
	
}
