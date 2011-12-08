package edu.uci.lighthouse.extensions.codereview.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.extensions.codereview.model.CodeReview;
import edu.uci.lighthouse.extensions.codereview.model.CodeReviewFacade;
import edu.uci.lighthouse.extensions.codereview.util.EditorUtility;

public class CloseReviewAction extends Action {
	private static final String ICON = "$nl$/icons/full/elcl16/deadlock_view.gif";
	private static final String DESCRIPTION = "Close Review";
	private StructuredViewer viewer;
	
	public CloseReviewAction(StructuredViewer viewer){
		this.viewer = viewer;
		init();
	}

	private void init() {
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.eclipse.jdt.debug.ui", ICON));
		setText(DESCRIPTION);
		setToolTipText(DESCRIPTION);
	}
	
	@Override
	public void run() {
		ISelection selection = viewer.getSelection();
		Object obj = ((IStructuredSelection)selection).getFirstElement();
		if (obj instanceof CodeReview) {
			CodeReviewFacade crf = new CodeReviewFacade();
			crf.setClose((CodeReview)obj);
		}
	}

//	@Override
//	public boolean isEnabled() {
//		ISelection selection = viewer.getSelection();
//		Object obj = ((IStructuredSelection)selection).getFirstElement();
//		if (obj instanceof CodeReview) {
//			CodeReview cr = (CodeReview) obj;
//			if (cr.getReviewee().equals(ModelUtility.getAuthor())) {
//				return true;
//			}
//		}
//		return false;
//	}
	
	
}
