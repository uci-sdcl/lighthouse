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

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.tigris.subversion.svnclientadapter.ISVNInfo;

import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.jpa.JPAException;

public class UpdateAction  extends CheckoutAction {

	private static final long serialVersionUID = -9051697002637243677L;
	
	public UpdateAction(Map<IFile, ISVNInfo> svnFiles) {
		super(svnFiles);
	}

	@Override
	public void run() throws JPAException {
		LighthouseModelManager modelManager = new LighthouseModelManager(LighthouseModel.getInstance());
		modelManager.removeArtifactsAndEvents(ModelUtility.getClassesFullyQualifiedName(getSVNFiles()));
		super.run();
	}

	@Override
	protected boolean checkDatabase() {
		return false;
	}
}
