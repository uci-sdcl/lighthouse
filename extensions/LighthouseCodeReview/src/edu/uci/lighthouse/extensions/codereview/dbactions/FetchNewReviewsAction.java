package edu.uci.lighthouse.extensions.codereview.dbactions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uci.lighthouse.core.dbactions.IPeriodicDatabaseAction;
import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.extensions.codereview.model.CodeReview;
import edu.uci.lighthouse.extensions.codereview.model.CodeReviewModel;
import edu.uci.lighthouse.extensions.codereview.model.CodeReviewModelManager;
import edu.uci.lighthouse.model.jpa.AbstractDAO;
import edu.uci.lighthouse.model.jpa.JPAException;

public class FetchNewReviewsAction implements IPeriodicDatabaseAction {

	private static final long serialVersionUID = 7058763265587652952L;

	@Override
	public void run() throws JPAException {
		CodeReviewModel model = CodeReviewModel.getInstance();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("reviewer", ModelUtility.getAuthor());
		parameters.put("lastId", model.getLastAddedReviewId());
		AbstractDAO<CodeReview, Integer> dao = new AbstractDAO<CodeReview, Integer>() {};
		List<CodeReview> reviews = dao.executeNamedQuery("CodeReview.getPendingReviews", parameters);
		CodeReviewModelManager mm = new CodeReviewModelManager(model);
		mm.addReviews(reviews);
	}

}
