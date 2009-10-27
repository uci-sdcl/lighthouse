package edu.uci.lighthouse.model.jpa;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
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

	/**
	 * Replacement Value from "." to "_";
	 */
	private static final String STRING_UNDERLINE = "_";

	/**
	 * Dot string that will be replaced in this kind of queries: entity.entity.name
	 */
	private static final String STRING_DOT = "\\.";

	private EntityManager entityManager;

	protected Class<T> type;
	
	@SuppressWarnings("unchecked")
	public AbstractDAO() {
		this.type = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	@SuppressWarnings("unchecked")
	public List<T> list() {
		List<T> resultados = entityManager.createQuery(
				"select entity from " + type.getSimpleName() + " entity")
				.getResultList();
		return resultados;
	}
 
	@SuppressWarnings("unchecked")
	public List<T> executeNamedQuery(String nameQuery,
			Map<String, Object> parameters) {
		Query query = entityManager.createNamedQuery(nameQuery);
		if (parameters != null) {
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				if (entry.getValue() instanceof Date) {
					query.setParameter(entry.getKey(), (Date) entry.getValue(),
							TemporalType.DATE);
				} else {
					query.setParameter(entry.getKey(), entry.getValue());
				}
			}
		}
		List resultados = query.getResultList();
		return resultados;
	}

	@SuppressWarnings("unchecked")
	public List<T> executeNamedQuery(String nameQuery, Object[] parameters) {
		Query query = entityManager.createNamedQuery(nameQuery);
		if (parameters != null) {
			int posicao = 1;
			for (Object valor : parameters) {
				if (valor instanceof Date) {
					query.setParameter(posicao++, (Date) valor,
							TemporalType.DATE);
				} else {
					query.setParameter(posicao++, valor);
				}
			}
		}
		List resultados = query.getResultList();
		return resultados;
	}

	/**
	 * Build a TOTALLY dynamically query.
	 * There are 2 possibles ways to set the entry.key:
	 * - "entity.fieldName"
	 * - "entity.innerEntity.fieldName"
	 */
	@SuppressWarnings("unchecked")
	public List<T> executeQuery(Map<String, Object> parameters){
		List<T> resultados = new ArrayList<T>();		
		StringBuffer queryBuf = new StringBuffer(
				"select distinct entity from " + type.getSimpleName()
						+ " entity ");
		boolean firstClause = true;
		
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			if (entry.getValue() instanceof String) {
				queryBuf.append(firstClause ? " where " : " and ");
				queryBuf
						.append("LOWER("+entry.getKey()+")" + " LIKE :" + entry.getKey().replaceAll(STRING_DOT, STRING_UNDERLINE));
					firstClause = false;
			} else {
				queryBuf.append(firstClause ? " where " : " and ");
				queryBuf.append(entry.getKey() + " = :" + entry.getKey().replaceAll(STRING_DOT, STRING_UNDERLINE));
				firstClause = false;
			}
		}

		String hqlQuery = queryBuf.toString();
		Query query = entityManager.createQuery(hqlQuery);
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			if (entry.getValue() instanceof Date) {
				query.setParameter(entry.getKey().replaceAll(STRING_DOT, STRING_UNDERLINE), (Date) entry.getValue(),
						TemporalType.DATE);
			} if(entry.getValue() instanceof String){
				query.setParameter(entry.getKey().replaceAll(STRING_DOT, STRING_UNDERLINE),((String)entry.getValue()).toLowerCase());
			} else {
				query.setParameter(entry.getKey().replaceAll(STRING_DOT, STRING_UNDERLINE), entry.getValue());
			}
		}
		resultados = query.getResultList();
		
		return resultados;
	}

	
	public T get(PK pk) {
		return entityManager.find(type, pk);
	}

	public void save(T entity) throws JPAUtilityException{		
		try {
			entityManager.persist(entity);
		} catch (RuntimeException e) {
			throw new JPAUtilityException("Error trying to save the entity: " + entity, e.fillInStackTrace());
		}
	}

	public T update(T entity) throws JPAUtilityException {
		try {
			return entityManager.merge(entity);
		} catch (RuntimeException e) {
			throw new JPAUtilityException("Error trying to update the entity: " + entity, e.fillInStackTrace());
		}
	}

	/* (non-Javadoc)
	 */
	public void remove(T entity) throws JPAUtilityException {
		try {
			Object toRemove = entityManager.merge(entity);
			entityManager.remove(toRemove);
		} catch (Exception e) {
			throw new JPAUtilityException("Error trying to remove the entity: " + entity, e.fillInStackTrace());
		}
	}

	/**
	 * Get entityManager.
	 * @return entityManager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * Set entityManager.
	 * @param entityManager {@link EntityManager}
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	/* (non-Javadoc)
	 */
	public void flush() {
		entityManager.flush();
	}

	/* (non-Javadoc)
	 */
	public void clear() {
		entityManager.clear();
	}
}
