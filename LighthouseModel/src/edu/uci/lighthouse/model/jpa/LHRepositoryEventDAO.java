/*******************************************************************************
* Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
*				, University of California, Irvine}.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    {Software Design and Collaboration Laboratory (SDCL)
*	, University of California, Irvine}
*			- initial API and implementation and/or initial documentation
*******************************************************************************/
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
