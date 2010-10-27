package edu.uci.lighthouse.core.dbactions.pull;

import java.util.List;

import org.apache.log4j.Logger;

import edu.uci.lighthouse.core.Activator;
import edu.uci.lighthouse.core.controller.PullModel;
import edu.uci.lighthouse.core.dbactions.IDatabaseAction;
import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.jpa.JPAException;

public class FetchNewEventsAction implements IDatabaseAction {
	
	private static Logger logger = Logger.getLogger(FetchNewEventsAction.class);
	
	private static final long serialVersionUID = 7200580843968876241L;

	@Override
	public void run() throws JPAException {
		/*
		 * If the map's size == 0, it means that it is the first time that user
		 * is running Lighthouse. Then, the LighthouseModel will be updated only
		 * if the user execute a checkout first.
		 */
		//if (getWorkingCopy().size() != 0) {
			LighthouseAuthor author = Activator.getDefault().getAuthor();
			PullModel pullModel = PullModel.getInstance();
			List<LighthouseEvent> events = pullModel.getNewEventsFromDB(author);
			if (events.size()>0) {
				ModelUtility.updateEvents(events);
				ModelUtility.fireModificationsToUI(events);
			} else {
				logger.debug("Events fecthed from database: " + events);
			}
		//}
	}


}
