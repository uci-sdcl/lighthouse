package edu.uci.lighthouse.extensions.codereview.dbactions;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;

import edu.uci.lighthouse.core.dbactions.IPeriodicDatabaseAction;
import edu.uci.lighthouse.extensions.codereview.model.CodeReview;
import edu.uci.lighthouse.extensions.codereview.model.CodeReview.StatusType;
import edu.uci.lighthouse.extensions.codereview.model.CodeReviewModel;
import edu.uci.lighthouse.extensions.codereview.model.CodeReviewModelManager;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.JPAUtility;

public class RefreshCurrentReviewsAction implements IPeriodicDatabaseAction {

	private static final long serialVersionUID = -5781942460527406028L;

	@Override
	public void run() throws JPAException {
		CodeReviewModel model = CodeReviewModel.getInstance();
		if (!model.isEmpty()) {
			CodeReviewModelManager mm = new CodeReviewModelManager(model);
			EntityManager em = JPAUtility.createEntityManager();
			List<CodeReview> toAddReviews = new LinkedList<CodeReview>();
			List<CodeReview> toRemoveReviews = new LinkedList<CodeReview>();
			for (CodeReview review : model.getReviews()) {
				CodeReview dbReview = em.find(CodeReview.class, review.getId());
				if (isToRemove(review)) {
					toRemoveReviews.add(dbReview);
				} else {
					toAddReviews.add(dbReview);
				}
			}
			mm.addReviews(toAddReviews);
			mm.removeReviews(toRemoveReviews);
			JPAUtility.closeEntityManager(em);
		}
	}
	
	private boolean isToRemove(CodeReview review) {
		return review.getStatus().equals(StatusType.CLOSE);
	}
}
