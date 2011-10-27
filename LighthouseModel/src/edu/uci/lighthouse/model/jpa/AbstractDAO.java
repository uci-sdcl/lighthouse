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

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 * Class responsible for all persistence actions.
 * 
 * @param <T> Entity that will be controlled.
 * @param <PK> primary key type
 *
 */
public abstract class AbstractDAO<T, PK extends Serializable> implements InterfaceDAO<T, PK> {

	protected Class<T> entityClass;
	
	@SuppressWarnings("unchecked")
	public AbstractDAO() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
	}
	
	@SuppressWarnings("unchecked")
	public synchronized List<T> list() throws JPAException {
		EntityManager entityManager = JPAUtility.createEntityManager();
		List<T> result = entityManager.createQuery(
				"select entity from " + entityClass.getSimpleName() + " entity")
				.getResultList();
		JPAUtility.closeEntityManager(entityManager);
		return result;
	}
 
	@SuppressWarnings("unchecked")
	public synchronized List<T> executeNamedQuery(String nameQuery,
			Map<String, Object> parameters) throws JPAException {
		EntityManager entityManager = JPAUtility.createEntityManager();
		Query query = entityManager.createNamedQuery(nameQuery);
		if (parameters != null) {
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				if (entry.getValue() instanceof Date) {
					query.setParameter(entry.getKey(), (Date) entry.getValue(),
							TemporalType.TIMESTAMP);
				} else {
					query.setParameter(entry.getKey(), entry.getValue());
				}
			}
		}
		List<T> result = query.getResultList();
		JPAUtility.closeEntityManager(entityManager);
		return result;
	}

	@SuppressWarnings("unchecked")
	public synchronized List<T> executeNamedQuery(String nameQuery, Object[] parameters) throws JPAException {
		EntityManager entityManager = JPAUtility.createEntityManager();
		Query query = entityManager.createNamedQuery(nameQuery);
		if (parameters != null) {
			int posicao = 1;
			for (Object valor : parameters) {
				if (valor instanceof Date) {
					query.setParameter(posicao++, (Date) valor,
							TemporalType.TIMESTAMP);
				} else {
					query.setParameter(posicao++, valor);
				}
			}
		}
		List result = query.getResultList();
		JPAUtility.closeEntityManager(entityManager);
		return result;
	}

	/**
	 * Methods that execute queries that are dynamically build by using the
	 * parameters
	 * 
	 * @param parameters
	 * @return
	 * @throws JPAException 
	 * @throws JPAQueryException
	 */
	@SuppressWarnings("unchecked")
	public synchronized List<T> executeDynamicQuery(String strQuery) throws JPAException {
		EntityManager entityManager = JPAUtility.createEntityManager();
		Query query = entityManager.createQuery(strQuery);
		List result = query.getResultList();
		JPAUtility.closeEntityManager(entityManager);
		return result;
	}
	
	public synchronized void executeUpdateQuery(String strQuery) throws JPAException {
		EntityManager entityManager = JPAUtility.createEntityManager();
		try {
			Query query = entityManager.createQuery(strQuery);
			JPAUtility.beginTransaction(entityManager);
			query.executeUpdate();
			JPAUtility.commitTransaction(entityManager);
		}	catch (RuntimeException e) {
			e.printStackTrace();
			throw new JPAException("Error trying to execute update the entity: " + strQuery, e.fillInStackTrace());
		}
		JPAUtility.closeEntityManager(entityManager);
	}
	
	public synchronized T get(PK pk) throws JPAException {
		EntityManager entityManager = JPAUtility.createEntityManager();
		T result = entityManager.find(entityClass, pk);
		JPAUtility.closeEntityManager(entityManager);
		return result;
	}

	public synchronized T save(T entity) throws JPAException {
		EntityManager entityManager = JPAUtility.createEntityManager();
		T result;
		try {
			JPAUtility.beginTransaction(entityManager);
			result = entityManager.merge(entity);
			JPAUtility.commitTransaction(entityManager);
		} catch (RuntimeException e) {
			throw new JPAException("Error trying to save/update the entity: " + entity, e.fillInStackTrace());
		}
		JPAUtility.closeEntityManager(entityManager);
		return result;
	}

	public synchronized void remove(T entity) throws JPAException {
		EntityManager entityManager = JPAUtility.createEntityManager();
		try {
			JPAUtility.beginTransaction(entityManager);
			Object toRemove = entityManager.merge(entity);
			entityManager.remove(toRemove);
			JPAUtility.commitTransaction(entityManager);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JPAException("Error trying to remove the entity: " + entity, e.fillInStackTrace());
		}
		JPAUtility.closeEntityManager(entityManager);
	}
	
}
