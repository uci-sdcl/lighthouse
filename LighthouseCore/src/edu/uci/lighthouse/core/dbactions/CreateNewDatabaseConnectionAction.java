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

import java.sql.SQLException;
import java.util.Properties;

import edu.uci.lighthouse.core.preferences.DatabasePreferences;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.JPAUtility;
import edu.uci.lighthouse.model.util.DatabaseUtility;

public class CreateNewDatabaseConnectionAction implements IDatabaseAction {

	private static final long serialVersionUID = -8387818046904494401L;

	Properties dbSettings;
	
	public CreateNewDatabaseConnectionAction(Properties dbSettings) {
		this.dbSettings = dbSettings;
	}

	@Override
	public void run() throws JPAException {
		try {
			DatabaseUtility.canConnect(dbSettings);
			JPAUtility.initializeEntityManagerFactory(DatabasePreferences
			.getDatabaseSettings());
		} catch (SQLException e) {
			throw new JPAException(e);
		}

	}

}
