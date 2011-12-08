package edu.uci.lighthouse.extensions.codereview.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.extensions.codereview.Activator;
import edu.uci.lighthouse.extensions.codereview.model.CodeReview;
import edu.uci.lighthouse.extensions.codereview.ui.ICodeReviewImages;

public class ReviewerFilterAction extends Action {
	private static final String DESCRIPTION = "I'm reviewing";
	private StructuredViewer viewer;
	
	public ReviewerFilterAction(StructuredViewer viewer){
		super(null, IAction.AS_CHECK_BOX);
		this.viewer = viewer;
		init();
	}

	private void init() {
		setImageDescriptor(Activator.getDefault().getImageRegistry().getDescriptor(ICodeReviewImages.IMG_REVIEW));
		setText(DESCRIPTION);
		setToolTipText(DESCRIPTION);
	}
	
	@Override
	public void run() {
		if (isChecked()){
			viewer.setFilters(new ViewerFilter[] {new ReviewerFilter()});
		} else {
			viewer.setFilters(new ViewerFilter[]{});
		}
	}
	
	private class ReviewerFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {
			if (element instanceof CodeReview) {
				CodeReview review = (CodeReview) element;
				if (!review.getReviewer().equals(ModelUtility.getAuthor()))   {
					return false;
				}
			}
			return true;
		}
		
	}
}
