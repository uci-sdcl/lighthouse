package edu.uci.lighthouse.core.dbactions.pull;

import java.util.List;

import edu.uci.lighthouse.core.Activator;
import edu.uci.lighthouse.core.controller.PullModel;
import edu.uci.lighthouse.core.dbactions.IDatabaseAction;
import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.jpa.JPAException;

public class FetchNewDataAction implements IDatabaseAction {
	
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
			ModelUtility.updateEvents(events);
			ModelUtility.fireModificationsToUI(events);
		//}
	}


}
