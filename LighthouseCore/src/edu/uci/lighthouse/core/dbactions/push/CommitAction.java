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
package edu.uci.lighthouse.core.dbactions.push;

import java.sql.SQLException;
import java.util.Collection;

import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.jpa.JPAException;

public class CommitAction extends FileEventAction {
	
	private static final long serialVersionUID = 4506601483048744221L;
	
	private boolean timeAdjusted = false;

	public CommitAction(Collection<LighthouseEvent> events) {
		super(events);
	}

	@Override
	public void run() throws JPAException {
		try {
			if (!timeAdjusted) {
				// Adjusts the committed time (from SVN) to the database's server time.
				// TODO TIAGO add a comment talking about optimization/cache
				ModelUtility.adjustCommittedTimeToServerTime(getEvents());
				timeAdjusted = true;
			}
			super.run();
		} catch (SQLException e) {
			throw new JPAException(e);
		}
	}
}
