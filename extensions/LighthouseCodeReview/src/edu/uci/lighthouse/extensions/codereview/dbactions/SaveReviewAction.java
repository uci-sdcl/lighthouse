package edu.uci.lighthouse.extensions.codereview.dbactions;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;

import edu.uci.lighthouse.core.dbactions.IDatabaseAction;
import edu.uci.lighthouse.extensions.codereview.model.CodeReview;
import edu.uci.lighthouse.extensions.codereview.model.CodeReviewModel;
import edu.uci.lighthouse.extensions.codereview.model.CodeReviewModelManager;
import edu.uci.lighthouse.extensions.codereview.model.CodeReview.StatusType;
import edu.uci.lighthouse.model.jpa.AbstractDAO;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.JPAUtility;

public class SaveReviewAction implements IDatabaseAction {

	private static final long serialVersionUID = 3242772609267270034L;
	
	private CodeReview review;
	
	public SaveReviewAction(CodeReview review){
		this.review = review;
	}
	
	@Override
	public void run() throws JPAException {
		CodeReviewModelManager mm = new CodeReviewModelManager(CodeReviewModel.getInstance());
		CodeReview dbReview = review;
		EntityManager em = JPAUtility.createEntityManager();
		JPAUtility.beginTransaction(em);
		try {
			em.persist(review);
		} catch (Exception e){
			em.getTransaction().rollback();
			JPAUtility.beginTransaction(em);
			dbReview = em.merge(review);
		}
		em.flush();
		em.refresh(dbReview);
		if (isToRemove(dbReview)){
			mm.removeReview(dbReview);
		} else {
			mm.addReview(dbReview);
		}
		JPAUtility.commitTransaction(em);
		JPAUtility.closeEntityManager(em);
	}
	
	private boolean isToRemove(CodeReview review) {
		return review.getStatus().equals(StatusType.CLOSE);
	}
}
