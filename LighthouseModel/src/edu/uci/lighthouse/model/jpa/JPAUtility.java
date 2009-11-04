package edu.uci.lighthouse.model.jpa;

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

	/**
	 * Entity Manager.
	 */
	private static EntityManager entityManager;

	/**
	 * Method that create EntityManager.
	 */
	private static void createEntityManager() {
		if (factoryEntityManager == null || !factoryEntityManager.isOpen())
			factoryEntityManager = Persistence
					.createEntityManagerFactory(PERSISTENCE_UNIT);
		entityManager = factoryEntityManager.createEntityManager();
	}

	/**
	 * Method that get the EntityManager.
	 * 
	 * @return {@link EntityManager} CRUD.
	 * @throws {@link JPAUtilityException} handle JPA errors.
	 */
	public synchronized static EntityManager getEntityManager() {
		if (entityManager == null || !entityManager.isOpen())
			createEntityManager();
		return entityManager;
	}

	/**
	 * Begin transaction.
	 */
	public static void beginTransaction() {
		if (entityManager != null && entityManager.isOpen())
			entityManager.getTransaction().begin();
	}

	/**
	 * Shutdown on Entity Manager.
	 */
	private static void shutdownEntityManager() {
		if (entityManager != null && entityManager.isOpen()) {
			entityManager.close();
		}
	}

	/**
	 * Shutdown on Entity Manager and Factory.
	 */
	public static void shutdownAndCloseFactory() {
		shutdownEntityManager();
		if (factoryEntityManager != null && factoryEntityManager.isOpen()) {
			factoryEntityManager.close();
		}
	}

	/**
	 * Commit Transaction
	 */
	public static void commitTransaction() {
		if (entityManager != null && entityManager.isOpen())
			entityManager.getTransaction().commit();
	}

	/**
	 * Rollback Transaction.
	 */
	public static void rollbackTransaction() {
		if (entityManager != null && entityManager.isOpen())
			entityManager.getTransaction().rollback();
	}

	/**
	 * Flush on Entity Manager.
	 */
	public void flush() {
		if (entityManager != null && entityManager.isOpen())
			entityManager.flush();
	}

	/**
	 * Clean entity manager.
	 */
	public void clear() {
		if (entityManager != null && entityManager.isOpen())
			entityManager.clear();
	}

}
