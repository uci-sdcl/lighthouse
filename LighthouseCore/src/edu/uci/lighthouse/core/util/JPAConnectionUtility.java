package edu.uci.lighthouse.core.util;

import java.util.Map;

import javax.persistence.EntityManager;

import edu.uci.lighthouse.core.preferences.DatabasePreferences;
import edu.uci.lighthouse.model.jpa.JPAUtility;

public class JPAConnectionUtility {

	public static boolean canConnect() {
		Map<String, String> dbSettings = DatabasePreferences.getDatabaseSettings();
		EntityManager entityManager = JPAUtility.createEntityManager(dbSettings);
		try { 
			entityManager.getTransaction().begin();
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			return false;
		} finally {
			JPAUtility.shutdownAndCloseFactory(entityManager);
		}
		return true;
	}
}
