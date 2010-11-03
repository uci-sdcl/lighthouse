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
