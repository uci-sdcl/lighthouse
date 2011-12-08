package edu.uci.lighthouse.extensions.codereview.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.apache.log4j.Logger;

public class CodeReviewModelManager {
	
	private CodeReviewModel model;
	
	private static Logger logger = Logger.getLogger(CodeReviewModelManager.class);

	public CodeReviewModelManager(CodeReviewModel model) {
		this.model = model;
	}
	
	public synchronized void addReview(CodeReview review){
		addReviews(Collections.singleton(review));
	}
	
	public synchronized void addReviews(Collection<CodeReview> reviews) {
		Collection<CodeReview> added = new LinkedList<CodeReview>();
		Collection<CodeReview> changed = new LinkedList<CodeReview>();
		for (CodeReview review : reviews) {
			CodeReview oldInstance = model.addReview(review);
			if (oldInstance == null) {
				added.add(review);
			}else if (!review.equals(oldInstance)) {
				changed.add(review);
			} else {
				logger.debug("not added");
			}
		}
		if (added.size() > 0){
			logger.debug("Reviews added: "+added.size());
			model.fireAdded(added);
		}
		if (changed.size() > 0){
			logger.debug("Reviews changed: "+changed.size());
			model.fireChanged(changed);
		}
	}
	
	public synchronized void removeReview(CodeReview review){
		removeReviews(Collections.singleton(review));
	}
	
	public synchronized void removeReviews(Collection<CodeReview> reviews) {
		Collection<CodeReview> removedReviews = new LinkedList<CodeReview>();
		for (CodeReview review : reviews) {
			CodeReview oldInstance = model.removeReview(review);
			if (oldInstance != null) {
				removedReviews.add(review);
			}
		}
		if (removedReviews.size() > 0) {
			logger.debug("Reviews removed: "+removedReviews.size());
			model.fireRemoved(removedReviews);
		}
	}
}
