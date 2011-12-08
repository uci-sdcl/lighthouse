package edu.uci.lighthouse.extensions.codereview.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.extensions.codereview.model.CodeReview;
import edu.uci.lighthouse.model.LighthouseAuthor;

public class TypeSorterAction extends Action {
	private static final String ICON = "$nl$/icons/elcl16/tree_explorer.gif";
	private static final String DESCRIPTION = "Sort by type";
	private StructuredViewer viewer;
	
	public TypeSorterAction(StructuredViewer viewer){
		super(null, IAction.AS_CHECK_BOX);
		this.viewer = viewer;
		init();
	}

	private void init() {
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.eclipse.ui.cheatsheets", ICON));
		setText(DESCRIPTION);
		setToolTipText(DESCRIPTION);
	}
	
	@Override
	public void run() {
		if (isChecked()){
			viewer.setSorter(new TypeSorter());
		} else {
			viewer.setSorter(new ViewerSorter());
		}
	}
	
	private static class TypeSorter extends ViewerSorter {

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			if (e1 instanceof CodeReview && e2 instanceof CodeReview) {
				CodeReview cr1 = (CodeReview) e1;
				CodeReview cr2 = (CodeReview) e2;
				LighthouseAuthor me = ModelUtility.getAuthor();
				if (me.equals(cr1.getReviewee())&&!me.equals(cr2.getReviewee())) {
					return -1;
				} else if (!me.equals(cr1.getReviewee())&&me.equals(cr2.getReviewee())) {
						return 1;
					
				}
			}
			return super.compare(viewer, e1, e2);
		}
	}
}

