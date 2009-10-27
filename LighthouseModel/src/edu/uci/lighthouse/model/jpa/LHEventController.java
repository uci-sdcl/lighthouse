package edu.uci.lighthouse.model.jpa;

import javax.persistence.EntityManager;

import edu.uci.lighthouse.model.LighthouseEvent;

/**
 * @author Nilmax
 */
public class LHEventController extends AbstractController<LighthouseEvent, Integer> {

	private static LHEventController instance;
 
	private LHEventController() {
	}

	public static LHEventController getInstance() {
		if (instance == null)
			instance = new LHEventController();
		return instance;
	}

	public InterfaceDAO<LighthouseEvent, Integer> getDAO(
			EntityManager entityManager) {
		LHEventDAO dao = new LHEventDAO();
		dao.setEntityManager(entityManager);
		return dao;
	}

}
