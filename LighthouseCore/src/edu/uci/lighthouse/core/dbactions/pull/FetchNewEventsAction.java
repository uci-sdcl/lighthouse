/*******************************************************************************
* Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
*				, University of California, Irvine}.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    {Software Design and Collaboration Laboratory (SDCL)
*	, University of California, Irvine}
*			- initial API and implementation and/or initial documentation
*******************************************************************************/
package edu.uci.lighthouse.core.dbactions.pull;

import java.util.List;

import org.apache.log4j.Logger;

import edu.uci.lighthouse.core.controller.PullModel;
import edu.uci.lighthouse.core.controller.UpdateLighthouseModel;
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
		LighthouseAuthor author = ModelUtility.getAuthor();
		PullModel pullModel = PullModel.getInstance();
		List<LighthouseEvent> events = pullModel.getNewEventsFromDB(author);
		if (events.size()>0) {
			UpdateLighthouseModel.addEvents(events);
			ModelUtility.fireModificationsToUI(events);
		} else {
			logger.debug("Events fecthed from database: " + events);
		}
	}

}
