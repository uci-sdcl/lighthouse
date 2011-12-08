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

public class RevieweeFilterAction extends Action {
	private static final String DESCRIPTION = "My reviews";
	private StructuredViewer viewer;
	
	public RevieweeFilterAction(StructuredViewer viewer){
		super(null, IAction.AS_CHECK_BOX);
		this.viewer = viewer;
		init();
	}

	private void init() {
		setImageDescriptor(Activator.getDefault().getImageRegistry().getDescriptor(ICodeReviewImages.IMG_ME));
		setText(DESCRIPTION);
		setToolTipText(DESCRIPTION);
	}
	
	@Override
	public void run() {
		if (isChecked()){
			viewer.setFilters(new ViewerFilter[] {new RevieweeFilter()});
		} else {
			viewer.setFilters(new ViewerFilter[]{});
		}
	}
	
	private class RevieweeFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {
			if (element instanceof CodeReview) {
				CodeReview review = (CodeReview) element;
				if (!review.getReviewee().equals(ModelUtility.getAuthor()))   {
					return false;
				}
			}
			return true;
		}
		
	}
}
