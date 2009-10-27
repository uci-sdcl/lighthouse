package edu.uci.lighthouse.model.jpa;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

/**
 * Class responsible to control the actions of other controllers.
 * 
 * @author Nilmax
 * 
 * @param <T>
 *            Bean that will be controlled.
 * @param <PK>
 *            primary key type
 */
public abstract class AbstractController<T, PK extends Serializable> implements
		InterfaceController<T, PK> {

	protected Class<T> type;

	@SuppressWarnings("unchecked")
	public AbstractController() {
		this.type = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public List<T> list() {
		EntityManager em = JPAUtility.getEntityManager();
		InterfaceDAO<T, PK> dao = getDAO(em);
		return dao.list();
	}

	public List<T> executeNamedQuery(String nameQuery,
			Map<String, Object> parameters) {
		EntityManager em = JPAUtility.getEntityManager();
		InterfaceDAO<T, PK> dao = getDAO(em);
		return dao.executeNamedQuery(nameQuery, parameters);
	}

	public List<T> executeNamedQuery(String nameQuery, Object[] parameters) {
		EntityManager em = JPAUtility.getEntityManager();
		InterfaceDAO<T, PK> dao = getDAO(em);
		return dao.executeNamedQuery(nameQuery, parameters);
	}

	public List<T> executeQuery(Map<String, Object> parameters) {
		EntityManager em = JPAUtility.getEntityManager();
		InterfaceDAO<T, PK> dao = getDAO(em);
		return dao.executeQuery(parameters);
	}

	public T get(PK pk) {
		EntityManager em = JPAUtility.getEntityManager();
		InterfaceDAO<T, PK> dao = getDAO(em);
		return dao.get(pk);
	}

	public void save(T entity) throws JPAUtilityException {
		EntityManager em = JPAUtility.getEntityManager();
		InterfaceDAO<T, PK> dao = getDAO(em);
		JPAUtility.beginTransaction();
		dao.save((T) entity);
		JPAUtility.commitTransaction();
	}
 
	public T update(T entity) throws JPAUtilityException { 
		EntityManager em = JPAUtility.getEntityManager();
		InterfaceDAO<T, PK> dao = getDAO(em);
		JPAUtility.beginTransaction();
		T result = dao.update((T) entity);
		JPAUtility.commitTransaction();
		return result;
	}

	public void remove(T entity) throws JPAUtilityException {
		EntityManager em = JPAUtility.getEntityManager();
		InterfaceDAO<T, PK> dao = getDAO(em);
		JPAUtility.beginTransaction();
		dao.remove((T) entity);
		JPAUtility.commitTransaction();
	}

	public abstract InterfaceDAO<T, PK> getDAO(EntityManager entityManager);

}
