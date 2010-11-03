package edu.uci.lighthouse.core.dbactions;

import java.sql.SQLException;
import java.util.Properties;

import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.util.DatabaseUtility;

public class CheckConnectivityAction implements IDatabaseAction {

	private static final long serialVersionUID = 6675946301376485527L;
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
