package edu.uci.lighthouse.model.jpa;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Nilmax
 * @param <T>
 * @param <PK>
 */
public interface InterfaceDAO<T, PK extends Serializable> {
	
	/**
	 * List all tuple of a particular entity
	 * @return
	 */
	public List<T> list();
	
	/**
	 * Return a list of <code>T</code> from a
	 * <code>query</code> pre-defined using namedQuery JPA notation
	 * @param query
	 * @param parameters query parameters
	 * @return
	 */
	public List<T> executeNamedQuery(String query,
			Map<String, Object> parameters);
	
	/**
	 * Return a list of <code>T</code> from a
	 * <code>query</code> pre-defined using namedQuery JPA notation
	 * @param query
	 * @param parameters query parameters
	 * @return
	 */
	public List<T> executeNamedQuery(String query, Object[] parameters);
	
	/**
	 * Execute query that are dynamically build using the parameters
	 * @param parameters
	 * @return
	 */
	public List<T> executeDynamicQuery(String strQuery);
	
	/**
	 * @param query
	 * @throws JPAUtilityException 
	 * */
	public void executeUpdateQuery(String strQuery) throws JPAUtilityException;
	
	/**
	 * Return an instance of <code>T</code> using the key <code>pk</code>
	 * @param pk
	 * @return
	 */
	public T get(PK pk);
	
	/**
	 * Save and UPDATE an instance of <code>T</code> in the database.
	 * Working with Detached entities
	 * @param entity
	 * @return T
	 * @throws JPAUpdateException
	 */
	public T save(T entity) throws JPAUtilityException;
	
	/**
	 * Remove an instance of <code>T</code>> from the data base.
	 * @param entity
	 * @throws JPADeleteException
	 */
	public void remove(T entity) throws JPAUtilityException;
	
	public Date getCurrentTimestamp();
	
	public void flush();

	public void clear();

}
