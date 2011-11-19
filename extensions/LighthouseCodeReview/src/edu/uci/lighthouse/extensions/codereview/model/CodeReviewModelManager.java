package edu.uci.lighthouse.extensions.codereview.model;

import java.util.Collection;
import java.util.Collections;

import org.apache.log4j.Logger;

public class CodeReviewModelManager {
	
	private CodeReviewModel model;
	
	private static Logger logger = Logger.getLogger(CodeReviewModelManager.class);

	public CodeReviewModelManager(CodeReviewModel model) {
		this.model = model;
	}
	
	public void addReview(CodeReview review){
		addReviews(Collections.singleton(review));
	}
	
	public void addReviews(Collection<CodeReview> reviews) {
		final int size = model.size();
		int updated = 0;
		for (CodeReview review : reviews) {
			CodeReview oldInstance = model.addReview(review);
			if (oldInstance != null && !review.equals(oldInstance)) {
				updated++;
			}			
		}
		int added = model.size()-size;
		
		logger.debug("Reviews added: "+added+" updated: "+updated);
		if (added > 0 || updated > 0) {
			logger.debug("Fire model changed.");
			model.fireModelChanged();
		}
	}
	
	public void removeReview(CodeReview review){
		removeReviews(Collections.singleton(review));
	}
	
	public void removeReviews(Collection<CodeReview> reviews) {
		int removed = 0;
		for (CodeReview review : reviews) {
			CodeReview oldInstance = model.removeReview(review);
			if (oldInstance != null) {
				removed++;
			}
		}
		logger.debug("Reviews removed: "+removed);
		if (removed > 0) {
			logger.debug("Fire model changed.");
			model.fireModelChanged();
		}
	}
}
