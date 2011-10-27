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
package edu.uci.lighthouse.core.dbactions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import edu.uci.lighthouse.model.jpa.JPAException;

public class CompoundAction implements IDatabaseAction {

	private static final long serialVersionUID = -7628446691313339816L;
	List<IDatabaseAction> actions = new LinkedList<IDatabaseAction>();
	
	@Override
	public void run() throws JPAException {
		for (IDatabaseAction action : actions){
			action.run();
		}
	}
	
	public void add(IDatabaseAction action) {
		actions.add(action);
	}
	
	public Collection<IDatabaseAction> getActions() {
		return actions;
	}
}
