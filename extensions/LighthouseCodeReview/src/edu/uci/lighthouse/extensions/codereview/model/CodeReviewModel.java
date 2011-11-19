package edu.uci.lighthouse.extensions.codereview.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CodeReviewModel {
	
	private Map<Integer, CodeReview> codeReviews = new HashMap<Integer, CodeReview>();
	
	private List<ICodeReviewModelListener> listeners = new LinkedList<ICodeReviewModelListener>();
	
	private int lastAddedReviewId = 0;

	private static CodeReviewModel instance;
	
	private CodeReviewModel() {
		// Singleton
	}
	
	public static CodeReviewModel getInstance(){
		if (instance == null) {
			instance = new CodeReviewModel();
		}
		return instance;
	}
	
	CodeReview addReview(CodeReview review){
		lastAddedReviewId = Math.max(lastAddedReviewId, review.getId());
		return codeReviews.put(review.getId(), review);
	}
	
	CodeReview removeReview(CodeReview review){
		return codeReviews.remove(review.getId());
	}

	public Iterable<CodeReview> getReviews(){
		return codeReviews.values();
	}
	
	public int getLastAddedReviewId() {
		return lastAddedReviewId;
	}
	
	public int size(){
		return codeReviews.size();
	}
	
	public boolean isEmpty() {
		return codeReviews.isEmpty();
	}
	
	public CodeReview getReview(int reviewId){
		return codeReviews.get(reviewId);
	}
	
	protected void fireModelChanged() {
		for (ICodeReviewModelListener l : listeners){
			l.modelChanged();
		}
	}
}
