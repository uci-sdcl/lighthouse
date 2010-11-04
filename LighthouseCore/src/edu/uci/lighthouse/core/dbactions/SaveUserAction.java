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
