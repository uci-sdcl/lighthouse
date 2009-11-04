package edu.uci.lighthouse.model.jpa;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 * Class responsible for all persistence actions.
 * 
 * @author Nilmax
 * 
 * @param <T> Entity that will be controlled.
 * @param <PK> primary key type
 *
 */
public abstract class AbstractDAO<T, PK extends Serializable> implements InterfaceDAO<T, PK> {

	protected EntityManager entityManager;

	protected Class<T> entityClass;
	
	@SuppressWarnings("unchecked")
	public AbstractDAO() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
		entityManager = JPAUtility.getEntityManager(); // TODO check this out just in case
	}
	
	@SuppressWarnings("unchecked")
	public synchronized List<T> list() {
		List<T> result = entityManager.createQuery(
				"select entity from " + entityClass.getSimpleName() + " entity")
				.getResultList();
		return result;
	}
 
	@SuppressWarnings("unchecked")
	public synchronized List<T> executeNamedQuery(String nameQuery,
			Map<String, Object> parameters) {
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
		return result;
	}

	@SuppressWarnings("unchecked")
	public synchronized List<T> executeNamedQuery(String nameQuery, Object[] parameters) {
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
		return result;
	}

	/**
	 * Methods that execute queries that are dynamically build by using the
	 * parameters
	 * 
	 * @param parameters
	 * @return
	 * @throws JPAQueryException
	 */
	@SuppressWarnings("unchecked")
	public synchronized List<T> executeDynamicQuery(String strQuery) {
		Query query = entityManager.createQuery(strQuery);
		return query.getResultList();
	}
	
	public synchronized void executeUpdateQuery(String strQuery) throws JPAUtilityException {
		try {
			Query query = entityManager.createQuery(strQuery);
			JPAUtility.beginTransaction();
			query.executeUpdate();
			JPAUtility.commitTransaction();
		}	catch (RuntimeException e) {
			throw new JPAUtilityException("Error trying to execute update the entity: " + strQuery, e.fillInStackTrace());
		}
	}
	
	public synchronized T get(PK pk) {
		return entityManager.find(entityClass, pk);
	}

	public synchronized T save(T entity) throws JPAUtilityException {
		T result;
		try {
			JPAUtility.beginTransaction();
			result = entityManager.merge(entity);
			JPAUtility.commitTransaction();
		} catch (RuntimeException e) {
			throw new JPAUtilityException("Error trying to save/update the entity: " + entity, e.fillInStackTrace());
		}
		return result;
	}

	/* (non-Javadoc)
	 */
	public synchronized void remove(T entity) throws JPAUtilityException {
		try {
			JPAUtility.beginTransaction();
			Object toRemove = entityManager.merge(entity);
			entityManager.remove(toRemove);
			JPAUtility.commitTransaction();
		} catch (Exception e) {
			throw new JPAUtilityException("Error trying to remove the entity: " + entity, e.fillInStackTrace());
		}
	}

	public synchronized Date getCurrentTimestamp() {
		Query query = entityManager.createQuery("SELECT CURRENT_TIMESTAMP FROM LighthouseEvent e WHERE e.id = 1");
		try {
			Object result = query.getSingleResult();
			return (Date) result;
		} catch (NoResultException e) {
			return new Date();
		}
	}
	
	/**
	 * Get entityManager.
	 * @return entityManager
	 */
	public synchronized EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * Set entityManager.
	 * @param entityManager {@link EntityManager}
	 */
	public synchronized void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	/* (non-Javadoc)
	 */
	public synchronized void flush() {
		entityManager.flush();
	}

	/* (non-Javadoc)
	 */
	public synchronized void clear() {
		entityManager.clear();
	}
}
