package edu.uci.lighthouse.model.jpa;

import java.util.HashMap;

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

	// This overrides the property in the persistence.xml file.
	private static HashMap<String, String> mapDatabaseProperties = new HashMap<String, String>();
	
	/**
	 * Factory that create Entity Manager.
	 */
	private static EntityManagerFactory factoryEntityManager;

	/**
	 * Method that create EntityManager.
	 */
	public static EntityManager createEntityManager() {
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
	
	public static void setMapDatabaseProperties(HashMap<String, String> mapDatabaseProperties) {
		JPAUtility.mapDatabaseProperties = mapDatabaseProperties;
	}

}
