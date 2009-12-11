package edu.uci.lighthouse.model.jpa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Utility persistence Class
 * 
 * @author Nilmax
 * 
 */
public class JPAUtility {

	/**
	 * Name of persistence unit.
	 */
	private static final String PERSISTENCE_UNIT = "LighthousePersistenceUnit";

	/**
	 * Factory that create Entity Manager.
	 */
	private static EntityManagerFactory factoryEntityManager;

	public static EntityManager createEntityManager() {
		return createEntityManager(new HashMap<String, String>());
	}
	
	/**
	 * Method that create EntityManager.
	 */
	public static EntityManager createEntityManager(Map<String, String> mapDatabaseProperties) {
		if (factoryEntityManager == null || !factoryEntityManager.isOpen()) {
			factoryEntityManager = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT,mapDatabaseProperties);
		}
		return factoryEntityManager.createEntityManager();
	}

	/**
	 * Begin transaction.
	 */
	public static void beginTransaction(EntityManager entityManager) {
		if (entityManager != null && entityManager.isOpen())
			entityManager.getTransaction().begin();
	}

	/**
	 * Shutdown on Entity Manager.
	 */
	public static void closeEntityManager(EntityManager entityManager) {
		if (entityManager != null && entityManager.isOpen()) {
			entityManager.close();
		}
	}

	/**
	 * Shutdown on Entity Manager and Factory.
	 */
	public static void shutdownAndCloseFactory(EntityManager entityManager) {
		closeEntityManager(entityManager);
		if (factoryEntityManager != null && factoryEntityManager.isOpen()) {
			factoryEntityManager.close();
		}
	}

	/**
	 * Commit Transaction
	 */
	public static void commitTransaction(EntityManager entityManager) {
		if (entityManager != null && entityManager.isOpen())
			entityManager.getTransaction().commit();
	}

	/**
	 * Rollback Transaction.
	 */
	public static void rollbackTransaction(EntityManager entityManager) {
		if (entityManager != null && entityManager.isOpen())
			entityManager.getTransaction().rollback();
	}

	/**
	 * Flush on Entity Manager.
	 */
	public void flush(EntityManager entityManager) {
		if (entityManager != null && entityManager.isOpen())
			entityManager.flush();
	}

	/**
	 * Clean entity manager.
	 */
	public void clear(EntityManager entityManager) {
		if (entityManager != null && entityManager.isOpen())
			entityManager.clear();
	}
	
	public static void canConnect(Properties dbSettings) throws SQLException {
		
		String url = dbSettings.getProperty("hibernate.connection.url");
		String username = dbSettings
				.getProperty("hibernate.connection.username");
		String password = dbSettings
				.getProperty("hibernate.connection.password");

		// Class.forName(dbSettings.getProperty("hibernate.connection.driver_class"));
		Connection conn = DriverManager.getConnection(url, username, password);
		conn.close();

	}
}
