package edu.uci.lighthouse.extensions.codereview.model;

import java.util.Collection;

public interface ICodeReviewModelListener {
	public void added(Collection<CodeReview> reviews);
	public void changed(Collection<CodeReview> reviews);
	public void removed(Collection<CodeReview> reviews);
}
