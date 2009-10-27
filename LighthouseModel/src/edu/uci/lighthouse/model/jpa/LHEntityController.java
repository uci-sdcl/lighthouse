package edu.uci.lighthouse.model.jpa;

import javax.persistence.EntityManager;

import edu.uci.lighthouse.model.LighthouseEntity;

/**
 * @author Nilmax
 */
public class LHEntityController extends AbstractController<LighthouseEntity, String> {

	private static LHEntityController instance;
 
	private LHEntityController() {
	}

	public static LHEntityController getInstance() {
		if (instance == null)
			instance = new LHEntityController();
		return instance;
	}

	public InterfaceDAO<LighthouseEntity, String> getDAO(
			EntityManager entityManager) {
		LHEntityDAO dao = new LHEntityDAO(); 
		dao.setEntityManager(entityManager);
		return dao;
	}

}
