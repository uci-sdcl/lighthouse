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

import java.util.Collection;

import edu.uci.lighthouse.core.dbactions.IDatabaseAction;
import edu.uci.lighthouse.model.LighthouseEvent;

@SuppressWarnings("serial")
public abstract class AbstractEventAction implements IDatabaseAction {

	private Collection<LighthouseEvent> events;

	public AbstractEventAction(Collection<LighthouseEvent> events) {
		this.events = events;
	}

	public Collection<LighthouseEvent> getEvents() {
		return events;
	}
}
