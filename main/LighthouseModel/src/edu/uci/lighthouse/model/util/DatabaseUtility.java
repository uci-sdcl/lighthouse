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
package edu.uci.lighthouse.model.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

/**
 * Utility class for database tasks.
 */
public class DatabaseUtility {

	private static TimeZone serverTimeZone;
	
	/**
	 * Returns a <code>Date</code> object adjusted for the given timezone.
	 * 
	 * @param dt The date to be adjusted.
	 * @param tz The desired timezone.
	 * @return
	 */
	public static Date getAdjustedDateTime(Date dt, TimeZone tz) {
		Date result = null;
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL,
				DateFormat.FULL);
		df.setTimeZone(tz);
		try {
			result = df.parse(df.format(dt));
		} catch (ParseException e) {
			// The exception will never be thrown, since we are parsing the same
			// pattern set in the DateFormat object.
		}
		return result;
	}

	/**
	 * Returns the Server timezone.
	 * 
	 * @param dbSettings
	 *            The database connection settings.
	 * @return A <code>TimeZone</code> object.
	 * @throws SQLException
	 */
	public static TimeZone getServerTimezone(Properties dbSettings)
			throws SQLException {
		if (serverTimeZone == null) {
			String url = dbSettings.getProperty("hibernate.connection.url");
			String username = dbSettings
					.getProperty("hibernate.connection.username");
			String password = dbSettings
					.getProperty("hibernate.connection.password");

			Connection conn = DriverManager.getConnection(url, username,
					password);

			ResultSet rs = conn
					.createStatement()
					.executeQuery(
							"select timestampdiff(HOUR,utc_timestamp(), current_timestamp());");
			String timediff = "";
			if (rs.next()) {
				timediff = rs.getString(1);
			}
			conn.close();

			serverTimeZone = TimeZone.getTimeZone("GMT" + timediff);
		}
		return serverTimeZone;
	}

	/**
	 * Returns whether is possible to connect to the database server for a given
	 * connection settings.
	 * 
	 * @param dbSettings
	 *            The datatabase connection settings
	 * @return <code>true</code> if is possible to connect to the database
	 *         server and <code>false</code> otherwise.
	 * @throws SQLException 
	 */
	public static void canConnect(Properties dbSettings) throws SQLException {
		String url = dbSettings.getProperty("hibernate.connection.url");
		String username = dbSettings
				.getProperty("hibernate.connection.username");
		String password = dbSettings
				.getProperty("hibernate.connection.password");
			Connection conn = DriverManager.getConnection(url, username,
					password);
			conn.close();
	}
	
}
