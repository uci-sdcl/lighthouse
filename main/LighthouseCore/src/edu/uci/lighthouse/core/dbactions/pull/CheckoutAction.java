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

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.tigris.subversion.svnclientadapter.ISVNInfo;

import edu.uci.lighthouse.core.controller.PullModel;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.jpa.JPAException;

public class CheckoutAction extends AbstractWorkingCopyAction {

	private static final long serialVersionUID = -435718370337302593L;
	
	private static Logger logger = Logger.getLogger(UpdateAction.class);

	public CheckoutAction(Map<IFile, ISVNInfo> svnFiles) {
		super(svnFiles);
	}

	@Override
	public void run() throws JPAException {
		PullModel pullModel = PullModel.getInstance();
		Collection<LighthouseEvent> events = pullModel.executeQueryCheckout(getWorkingCopy());
		logger.debug("Number of events fetched: " + events.size());
		LighthouseModel.getInstance().fireModelChanged();
	}

	@Override
	protected boolean checkDatabase() {
		return true;
	}
	
}
