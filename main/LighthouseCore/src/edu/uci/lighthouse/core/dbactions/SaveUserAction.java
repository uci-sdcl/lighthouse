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

import java.util.Properties;

import edu.uci.lighthouse.core.preferences.UserPreferences;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.LHAuthorDAO;

public class SaveUserAction implements IDatabaseAction {

	@Override
	public void run() throws JPAException {
		Properties userSettings = UserPreferences.getUserSettings();
		String userName = userSettings.getProperty(UserPreferences.USERNAME);
		LighthouseAuthor user = new LighthouseAuthor(userName);
		new LHAuthorDAO().save(user);
	}

}
