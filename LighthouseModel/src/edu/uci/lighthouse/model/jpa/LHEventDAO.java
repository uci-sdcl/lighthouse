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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseEvent.TYPE;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.util.LHStringUtil;

public class LHEventDAO extends AbstractDAO<LighthouseEvent, String> {
	
	private static Logger logger = Logger.getLogger(LHEventDAO.class);

	/**
	 * Execute a query in the database in order to return the events related
	 * with a given java file version
	 * 
	 * If you want to have a better idea of this query you need to look for
	 * the file "Query Checkout Example.png" in the lighthouse documents folder
	 * 
	 * @param listEntitiesInside
	 * @param revisionTime
	 * @return
	 * @throws JPAException
	 */
	public List<LighthouseEvent> executeQueryCheckOut(
			Collection<LighthouseEntity> listEntitiesInside,
			Date revisionTime) throws JPAException {
		List<LighthouseEvent> result = new LinkedList<LighthouseEvent>();
		if (listEntitiesInside.size()>0) {			
			String strQuery = "SELECT e " + "FROM LighthouseEvent e "
			+ "WHERE ";
			strQuery += " ( ";
			// Get all entities' events
			for (LighthouseEntity entity : listEntitiesInside) {
				String id = entity.getId();
				id = id.replaceAll("\\,", "\\\\,");
				String strTimestamp = LHStringUtil.simpleDateFormat.format(revisionTime);
				strQuery += " ( ( ";
				strQuery += "e.entity" + " = " + "'" + id + "'";
				strQuery += " AND ";
				strQuery += "e.timestamp" + " >= " + "'" + strTimestamp + "'";
				strQuery += " ) OR ( ( ";
				strQuery += "e.entity" + " = " + "'" + id + "'";
				strQuery += " AND ";
				strQuery += "e.isCommitted" + " = " + true;
				strQuery += " AND ";
				strQuery += "e.committedTime" + " <= " + "'" + strTimestamp + "'";
				strQuery += " ) AND ( ";
				strQuery += "e.type" + " = " + TYPE.ADD.ordinal();
				strQuery += " OR ";
				strQuery += "e.type" + " = " + TYPE.REMOVE.ordinal();
				strQuery += " ) ) ) ";
				strQuery += "OR ";
			}
			// Get all relationships' events
			for (LighthouseEntity entity : listEntitiesInside) {
				String id = entity.getId();
				id = id.replaceAll("\\,", "\\\\,");
				String strTimestamp = LHStringUtil.simpleDateFormat.format(revisionTime);
				strQuery += " ( ( ";
				strQuery += " ( ";
				strQuery += "e.relationship.primaryKey.from" + " = " + "'" + id + "'";
				strQuery += " OR ";
				strQuery += "e.relationship.primaryKey.to" + " = " + "'" + id + "'";
				strQuery += " ) ";
				strQuery += " AND ";
				strQuery += "e.timestamp" + " >= " + "'" + strTimestamp + "'";
				strQuery += " ) OR ( ( ";
				strQuery += " ( ";
				strQuery += "e.relationship.primaryKey.from" + " = " + "'" + id + "'";
				strQuery += " OR ";
				strQuery += "e.relationship.primaryKey.to" + " = " + "'" + id + "'";
				strQuery += " ) ";
				strQuery += " AND ";
				strQuery += "e.isCommitted" + " = " + true;
				strQuery += " AND ";
				strQuery += "e.committedTime" + " <= " + "'" + strTimestamp + "'";
				strQuery += " ) AND ( ";
				strQuery += "e.type" + " = " + TYPE.ADD.ordinal();
				strQuery += " OR ";
				strQuery += "e.type" + " = " + TYPE.REMOVE.ordinal();
				strQuery += " ) ) ) ";
				strQuery += "OR ";
			}
			strQuery = strQuery.substring(0, strQuery.lastIndexOf("OR"));
			strQuery += " )";
			List<LighthouseEvent> queryResult = executeDynamicQuery(strQuery);
			result = (queryResult!=null) ? queryResult : result;
		}
		return result;
	}

	public void saveListEvents(Collection<LighthouseEvent> listEvents, IProgressMonitor monitor) throws JPAException {
		EntityManager entityManager = null;
		if (monitor == null){
			monitor = new NullProgressMonitor();
		}
		try {
			monitor.beginTask("Saving in the database...", listEvents.size());
			final int INC = (int)(listEvents.size() * 0.025) + 1; // (+1) avoid division by zero
			int i = 0;
			
			entityManager = JPAUtility.createEntityManager();
			JPAUtility.beginTransaction(entityManager);
			
			// for each entity event
			for (LighthouseEvent event : listEvents) {
				if (monitor.isCanceled()) {
					throw new OperationCanceledException();
				}
				Object artifact = event.getArtifact();
				if (artifact instanceof LighthouseEntity) {
					if (event.isCommitted()) {
						event = updateCommittedEvent(entityManager, event);
					}
					event = entityManager.merge(event);
					if (i % INC == 0) {
						monitor.worked(INC);
					}
					logger.debug("Add event in database: " + event);
				}
				i++;
			}
			
			// for each relationship event
			for (LighthouseEvent event : listEvents) {
				if (monitor.isCanceled()){
					throw new OperationCanceledException();
				}
				Object artifact = event.getArtifact();
				if (artifact instanceof LighthouseRelationship) {
					if (event.isCommitted()) {
						event = updateCommittedEvent(entityManager, event);
					}
					event = entityManager.merge(event);
					if (i % INC == 0) {
						monitor.worked(INC);
					}
					logger.debug("Add event in database: " + event);
				}
				i++;
			}
			
			JPAUtility.commitTransaction(entityManager);
			JPAUtility.closeEntityManager(entityManager);
			
		} catch (OperationCanceledException e) {
			logger.error(e,e);
			JPAUtility.rollbackTransaction(entityManager);
			throw e;
		} catch (PersistenceException e) {
			logger.error(e,e);
			JPAUtility.rollbackTransaction(entityManager);
			throw new JPAException("Error trying to save/update the event", e.fillInStackTrace());
		} catch (RuntimeException e) {
			logger.error(e,e);
			throw new JPAException("Error with database connection", e.fillInStackTrace());
		} finally {
			monitor.done();
		}
	}

	private LighthouseEvent updateCommittedEvent(EntityManager entityManager, LighthouseEvent event) {
		LighthouseEvent dbEvent = entityManager.find(entityClass, event.getId());
		if (dbEvent!=null) {
			dbEvent.setCommitted(event.isCommitted());
			dbEvent.setCommittedTime(event.getCommittedTime());
			Date timestamp = dbEvent.getTimestamp();
			Date committedTime = dbEvent.getCommittedTime();
			if (timestamp.after(committedTime)) {
				dbEvent.setTimestamp(committedTime);
			}
			return dbEvent;
		} else {
			return event;
		}
	}
	
}
