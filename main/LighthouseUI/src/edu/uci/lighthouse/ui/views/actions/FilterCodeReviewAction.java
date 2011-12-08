package edu.uci.lighthouse.ui.views.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.GraphViewer;

import edu.uci.lighthouse.extensions.codereview.model.CodeReview;
import edu.uci.lighthouse.extensions.codereview.model.CodeReviewModel;
import edu.uci.lighthouse.extensions.codereview.model.CodeSelection;
import edu.uci.lighthouse.extensions.codereview.model.FileSnapshot;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseInterface;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.ui.views.FilterManager;

public class FilterCodeReviewAction extends Action implements ISelectionListener{
	protected GraphViewer viewer;
	
	private static final String ICON = "$nl$/icons/review.gif";
	private static final String DESCRIPTION = "Filter by code review";
	
	private CodeReview selectedReview;
	private CodeReviewFilter filter  = new CodeReviewFilter();
	
	private static Logger logger = Logger.getLogger(FilterCodeReviewAction.class);

	public FilterCodeReviewAction(GraphViewer viewer){
		super(null, IAction.AS_CHECK_BOX);
		this.viewer = viewer;
		init();
	}
	
	private void init() {
		setToolTipText(DESCRIPTION);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"edu.uci.lighthouse.extensions.codereview", ICON));
	}
	
	@Override
	public void run() {
		if (isChecked()){
			FilterManager.getInstance().addViewerFilter(filter);
		} else {
			FilterManager.getInstance().removeViewerFilter(filter);
		}
	}
	
	private class CodeReviewFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {
			if (element instanceof LighthouseClass || element instanceof LighthouseInterface) {
				LighthouseEntity entity = (LighthouseEntity) element;
//				entity.getShortName()
				if (selectedReview != null){
				for (FileSnapshot fs: selectedReview.getFilesSnapshot()){
					if (entity.getShortName().equals(fs.getFilename().replaceAll(".java", ""))) {
						return true;
					}
				}
				}
			} else if (element instanceof LighthouseRelationship || element instanceof EntityConnectionData){
				return true;
			} 
			return false;
		}
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		CodeReviewModel model = CodeReviewModel.getInstance();
			Object obj = ((TreeSelection) selection).getFirstElement();
			if (obj instanceof CodeReview) {
				selectedReview = (CodeReview) obj;
			} else if (obj instanceof FileSnapshot) {
				selectedReview = model.getReview((FileSnapshot)obj);
			} else if (obj instanceof CodeSelection) {
				selectedReview = model.getReview((CodeSelection)obj);
			}
		if (isChecked()) {
			viewer.refresh();
		}
			
			
	}
}
