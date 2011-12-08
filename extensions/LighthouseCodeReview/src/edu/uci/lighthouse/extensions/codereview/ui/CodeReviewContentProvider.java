package edu.uci.lighthouse.extensions.codereview.ui;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import edu.uci.lighthouse.extensions.codereview.model.CodeReview;
import edu.uci.lighthouse.extensions.codereview.model.CodeReviewModel;
import edu.uci.lighthouse.extensions.codereview.model.CodeSelection;
import edu.uci.lighthouse.extensions.codereview.model.Comment;
import edu.uci.lighthouse.extensions.codereview.model.FileSnapshot;

public class CodeReviewContentProvider implements ITreeContentProvider {

	private CodeReviewModel model;
	
	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput instanceof CodeReviewModel) {
			model = (CodeReviewModel) newInput;
		}
	}

	@Override
	public Object[] getElements(Object inputElement) {
		Object[] result = new Object[]{};
		if (inputElement instanceof CodeReviewModel){
			result = model.getReviews().toArray();
		}
		return result;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		Object[] result = new Object[]{};
		/*if (parentElement instanceof CodeReviewModel) {
			result = model.getReviews().toArray();
		} else*/ if (parentElement instanceof CodeReview) {
			result =   ((CodeReview)parentElement).getFilesSnapshot().toArray();
		} else if (parentElement instanceof FileSnapshot) {
			result = ((FileSnapshot)parentElement).getCodeSelection().toArray();
		}
//		} else if (parentElement instanceof CodeSelection){
//			result = ((CodeSelection)parentElement).getComments().toArray();
//		}
		return result;
	}

	@Override
	public Object getParent(Object element) {
		Object result = null;
		/*if (element instanceof CodeReview) {
			result = model;
		}else*/ if (element instanceof FileSnapshot) {
			result = model.getReview((FileSnapshot)element);
		} else if (element instanceof CodeSelection) {
			result = model.getFileSnapshot((CodeSelection)element);
		} /*else if (element instanceof Comment){
			result = model.getCodeSelection((Comment)element);
		}*/
		return result;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

}
