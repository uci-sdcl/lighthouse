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
package edu.uci.lighthouse.model.test;

import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

import edu.uci.lighthouse.model.util.DatabaseUtility;

public class DatabaseUtilityTest {
	
	private final String KEY_URL = "hibernate.connection.url";
	private final String KEY_USERNAME = "hibernate.connection.username";
	private final String KEY_PASSWORD = "hibernate.connection.password";

	@Test
	public void testGetAdjustedDateTime() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetServerTimezone() {
	//	fail("Not yet implemented");
	}

	@Test
	public void testCanConnect() {
		Properties dbSettings = new Properties();
		dbSettings.put(KEY_USERNAME, "lighthouse");
		dbSettings.put(KEY_PASSWORD, "light99");
		
		dbSettings.put(KEY_URL, "jdbc:mysql://calico.ics.uci.edu:3306/lighthouse?autoReconnect=true");
		try {
			DatabaseUtility.canConnect(dbSettings);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// For fake hosts are very fast.
		dbSettings.put(KEY_URL, "jdbc:mysql://calico2.ics.uci.edu:3306/lighthouse?autoReconnect=true");
		try {
			DatabaseUtility.canConnect(dbSettings);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// For know hosts its very fast.
		dbSettings.put(KEY_URL, "jdbc:mysql://www.ics.uci.edu:3306/lighthouse?autoReconnect=true");
		try {
			DatabaseUtility.canConnect(dbSettings);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
