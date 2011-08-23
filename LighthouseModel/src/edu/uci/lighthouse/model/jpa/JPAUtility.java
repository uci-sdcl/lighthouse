package edu.uci.lighthouse.model.jpa;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.hibernate.ejb.Ejb3Configuration;

import edu.uci.lighthouse.LHmodelExtensions.ClassPluginLoader;
import edu.uci.lighthouse.LHmodelExtensions.LHclassPluginExtension;

/**
 * Utility persistence Class
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
	
	// For shutdown purposes
	private static Collection<EntityManagerFactory>cacheFactory = new LinkedList<EntityManagerFactory>();
	
	private static Logger logger = Logger.getLogger(JPAUtility.class);
	
	public static void initializeEntityManagerFactory(Properties dbSettings) {
		logger.info("initializing EntityManagerFactory...");
		try {
			EntityManagerFactory factory = makeEntityFactory(dbSettings);
			cacheFactory.add(factory);
			// Just change the reference if no RuntimeException is throw
			factoryEntityManager = factory;
		} catch (RuntimeException e) {
			logger.error(e,e);
		}
	}
	
	/**
	 * @author lee
	 * @return
	 */
	private static EntityManagerFactory makeEntityFactory(Properties dbSettings){
		
		
		   
		   EntityManagerFactory factory = null;
		
		   
	
		  factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, dbSettings);
		
	/*
		
		List<LHclassPluginExtension> listOfExt = 
			ClassPluginLoader.getInstance().loadClassPluginExtensions();
		
		Ejb3Configuration cfg = new Ejb3Configuration();
		
		  cfg.addProperties( dbSettings ) //add some properties    
		     .addAnnotatedClass( edu.uci.lighthouse.model.LighthouseClass.class ) //add a class to be mapped
		     .addAnnotatedClass( edu.uci.lighthouse.model.LighthouseExternalClass.class ) //add a class to be mapped
		     .addAnnotatedClass( edu.uci.lighthouse.model.LighthouseField.class ) //add a class to be mapped
		     .addAnnotatedClass( edu.uci.lighthouse.model.LighthouseInterface.class ) //add a class to be mapped
		     .addAnnotatedClass( edu.uci.lighthouse.model.LighthouseMethod.class ) //add a class to be mapped
		     .addAnnotatedClass( edu.uci.lighthouse.model.LighthouseModifier.class ) //add a class to be mapped
		     .addAnnotatedClass( edu.uci.lighthouse.model.LighthouseRelationship.class ) //add a class to be mapped
		     .addAnnotatedClass( edu.uci.lighthouse.model.LighthouseEvent.class ) //add a class to be mapped
		     .addAnnotatedClass( edu.uci.lighthouse.model.LighthouseAuthor.class ) //add a class to be mapped
		     .addAnnotatedClass( edu.uci.lighthouse.model.repository.LighthouseRepositoryEvent.class ) //add a class to be mapped
		     .addAnnotatedClass( edu.uci.lighthouse.model.expertise.LighthouseQuestion.class ); //add a class to be mapped
		     
		  
		     for(LHclassPluginExtension ext: listOfExt){
				System.out.println(ext.getClass().getName());			
		    	 cfg.addAnnotatedClass(ext.getClass());
		     }

		     factory = cfg.buildEntityManagerFactory(); //Create the entity manager factory*/
		
		
		return factory;
	}
	
	public static void shutdownEntityManagerFactory() {
		logger.info("shutdown EntityManagerFactory...");
		try {
			for (EntityManagerFactory factory : cacheFactory) {
				if (factory.isOpen()) {
					factory.close();
				}
			}
		} catch (RuntimeException e) {
			logger.error(e,e);
		}
	}
	
	public static EntityManager createEntityManager() throws JPAException {
		if (factoryEntityManager == null || !factoryEntityManager.isOpen()) {
			initializeEntityManagerFactory(new Properties());
		}
		return factoryEntityManager.createEntityManager();
	}
	
	/**
	 * Method that create EntityManager.
	 */
	public static EntityManager createEntityManager(Properties mapDatabaseProperties) {
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
