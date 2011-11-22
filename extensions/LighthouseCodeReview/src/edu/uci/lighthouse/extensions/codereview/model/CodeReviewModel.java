package edu.uci.lighthouse.extensions.codereview.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

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

	public Collection<CodeReview> getReviews(){
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
	
	public CodeReview getReview(FileSnapshot fs) {
		int id = fs.getId();
		Collection<CodeReview> reviews = codeReviews.values();
		for (CodeReview review: reviews) {
			if (review.getFileSnapshotById(id) != null) {
				return review;
			}
		}
		return null;
	}
	
	public CodeReview getReview(Comment comment) {
		int id = comment.getId();
		Collection<CodeReview> reviews = codeReviews.values();
		for (CodeReview review: reviews) {
			Collection<FileSnapshot> filesSnapshots = review.getFilesSnapshot();
			for (FileSnapshot fs: filesSnapshots){
				Collection<CodeSelection> selections = fs.getCodeSelection();
				for (CodeSelection s: selections) {
					if (s.getCommentById(id)!= null) {
						return review;
					}
				}
			}
		}
		return null;
	}
	
	public CodeReview getReview(CodeSelection selection) {
		int id = selection.getId();
		Collection<CodeReview> reviews = codeReviews.values();
		for (CodeReview review: reviews) {
			Collection<FileSnapshot> filesSnapshots = review.getFilesSnapshot();
			for (FileSnapshot fs: filesSnapshots){
				if (fs.getCodeSelectionById(id)!= null) {
					return review;
				}
			}
		}
		return null;
	}
	
	public FileSnapshot getFileSnapshot(CodeSelection selection){
		int id = selection.getId();
		Collection<CodeReview> reviews = codeReviews.values();
		for (CodeReview review: reviews) {
			Collection<FileSnapshot> filesSnapshots = review.getFilesSnapshot();
			for (FileSnapshot fs: filesSnapshots){
				if (fs.getCodeSelectionById(id)!= null) {
					return fs;
				}
			}
		}
		return null;
	}
	
	public CodeSelection getCodeSelection(Comment comment){
		int id = comment.getId();
		Collection<CodeReview> reviews = codeReviews.values();
		for (CodeReview review: reviews) {
			Collection<FileSnapshot> filesSnapshots = review.getFilesSnapshot();
			for (FileSnapshot fs: filesSnapshots){
				Collection<CodeSelection> selections = fs.getCodeSelection();
				for (CodeSelection s: selections) {
					if (s.getCommentById(id)!= null) {
						return s;
					}
				}
			}
		}
		return null;
	}
	
	protected void fireAdded(Collection<CodeReview> changedReviews) {
		for (ICodeReviewModelListener l : listeners){
			l.added(changedReviews);
		}
	}
	
	protected void fireChanged(Collection<CodeReview> changedReviews) {
		for (ICodeReviewModelListener l : listeners){
			l.changed(changedReviews);
		}
	}
	
	protected void fireRemoved(Collection<CodeReview> changedReviews) {
		for (ICodeReviewModelListener l : listeners){
			l.removed(changedReviews);
		}
	}
	
	public void addCodeReviewModelListener(ICodeReviewModelListener listener){
		listeners.add(listener);
	}
	
	public void removeCodeReviewModelListener(ICodeReviewModelListener listener){
		listeners.remove(listener);
	}
}
