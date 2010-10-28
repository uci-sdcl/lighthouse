package edu.uci.lighthouse.core.dbactions;

import java.sql.SQLException;
import java.util.Properties;

import edu.uci.lighthouse.core.preferences.DatabasePreferences;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.util.DatabaseUtility;

public class CheckConnectivityAction implements IDatabaseAction {

	Properties dbSettings;
	
	public CheckConnectivityAction(Properties dbSettings) {
		this.dbSettings = dbSettings;
	}

	@Override
	public void run() throws JPAException {
		try {
			DatabaseUtility.canConnect(dbSettings);
		} catch (SQLException e) {
			throw new JPAException(e);
		}

	}

}
