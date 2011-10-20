package edu.uci.lighthouse.ui.views.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.ui.views.FilterManager;
import edu.uci.lighthouse.ui.views.IEditorSelectionListener;
import edu.uci.lighthouse.views.filters.ActiveClassFilter;

public class FilterActiveClassAction extends Action implements IEditorSelectionListener{

	//FIXME: Not using container (graph or viewer)
	protected GraphViewer viewer;
	
	private static final String ICON = "$nl$/icons/full/clcl16/collapseall.gif";
	private static final String DESCRIPTION = "Show the active class and its dependencies";

	private static Logger logger = Logger.getLogger(FilterActiveClassAction.class);
	
	ActiveClassFilter filter = new ActiveClassFilter();
	
	LayoutAlgorithm currentLayoutAlgorithm ;
	LayoutAlgorithm layoutAlgorithm = new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	
	LighthouseEntity lastActiveClass;
	
	public FilterActiveClassAction(GraphViewer viewer){
		super(null, IAction.AS_CHECK_BOX);
		this.viewer = viewer;
		init();
	}
	
	private void init() {
		setToolTipText(DESCRIPTION);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.eclipse.ui.navigator.resources", ICON));
	}
	
	@Override
	public void run() {
		if (isChecked()){
			currentLayoutAlgorithm = viewer.getGraphControl().getLayoutAlgorithm();
			FilterManager.getInstance().addViewerFilter(filter);
			viewer.setLayoutAlgorithm(layoutAlgorithm, true);
			lastActiveClass = filter.getLighthouseEntityFromEditor();
		} else {
			FilterManager.getInstance().removeViewerFilter(filter);
			viewer.setLayoutAlgorithm(currentLayoutAlgorithm);
		}
	}

	@Override
	public void selectionChanged(IFile editedFile) {
		logger.info("selectionChanged");
		if (isChecked()) {
			LighthouseEntity c = filter.getLighthouseEntityFromEditor();
			if (c != null && !c.equals(lastActiveClass)) {
				viewer.refresh();
				viewer.setLayoutAlgorithm(layoutAlgorithm, true);
				lastActiveClass = c;
			}
		}
	}

	@Override
	public void editorClosed() {
		lastActiveClass = null;
		viewer.refresh();
	}

}
