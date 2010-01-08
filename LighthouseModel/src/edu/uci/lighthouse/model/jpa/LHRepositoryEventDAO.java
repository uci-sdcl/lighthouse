package edu.uci.lighthouse.model.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;

import edu.uci.lighthouse.model.repository.LighthouseRepositoryEvent;

public class LHRepositoryEventDAO extends AbstractDAO<LighthouseRepositoryEvent, Integer> {

	private static Logger logger = Logger.getLogger(LHRepositoryEventDAO.class);
	
	public void saveRepositoryEvents(
			Collection<LighthouseRepositoryEvent> listRepEvents) throws JPAException {
		EntityManager entityManager = null;
		try {
			entityManager = JPAUtility.createEntityManager();
			JPAUtility.beginTransaction(entityManager);
			for (LighthouseRepositoryEvent repEvent : listRepEvents) {
				entityManager.merge(repEvent);
//				logger.debug("Add repository event in database: " + repEvent);
			}
			JPAUtility.commitTransaction(entityManager);
			JPAUtility.closeEntityManager(entityManager);
		} catch (PersistenceException e) {
			JPAUtility.rollbackTransaction(entityManager);
			throw new JPAException("Error trying to save/update the repository event", e.fillInStackTrace());
		} catch (RuntimeException e) {
			throw new JPAException("Error with database connection", e.fillInStackTrace());
		}
		
	}

}
