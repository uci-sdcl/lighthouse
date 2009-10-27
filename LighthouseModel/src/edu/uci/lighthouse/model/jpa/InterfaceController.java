package edu.uci.lighthouse.model.jpa;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

/**
 * Interface with all methods responsible for control DAO actions
 * 
 * @author Nilmax
 * 
 * @param <T>
 * @param <PK>
 */
public interface InterfaceController<T, PK extends Serializable> {

	/**
	 * List all tuples of a particular entity
	 * 
	 * @return
	 */
	public List<T> list();

	/**
	 * Return list of <code>T</code> that follow the criteria defined by the
	 * query <code>query</code>
	 * 
	 * @param query
	 * @param parameters
	 *            query parameters
	 * @return
	 */
	public List<T> executeNamedQuery(String query, Map<String, Object> parameters);

	/**
	 * Return list of <code>T</code> that follow the criteria defined by the
	 * query <code>query</code>
	 * 
	 * @param query
	 * @param parameters
	 * @return
	 */
	public List<T> executeNamedQuery(String query, Object[] parameters);

	/**
	 * Methods that execute queries that are dynamically build by using the
	 * parameters
	 * 
	 * @param parameters
	 * @return
	 * @throws JPAQueryException
	 */
	public List<T> executeQuery(Map<String, Object> parameters);

	/**
	 * Return a list of <code>T</code> by using the key <code>PK</code>
	 * 
	 * @param pk
	 * @return
	 */
	public T get(PK pk);

	public void save(T entity) throws JPAUtilityException;

	public T update(T entity) throws JPAUtilityException;

	public void remove(T entity) throws JPAUtilityException;

	public InterfaceDAO<T, PK> getDAO(EntityManager entityManager);
}
