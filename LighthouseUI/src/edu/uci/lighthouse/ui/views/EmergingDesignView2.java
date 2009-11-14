package edu.uci.lighthouse.ui.views;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;

import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.ui.views.actions.DiagramModeDropDownAction;
import edu.uci.lighthouse.ui.views.actions.FilterAuthorAction;
import edu.uci.lighthouse.ui.views.actions.FilterModifiedAction;
import edu.uci.lighthouse.ui.views.actions.HighlightDropDownAction;
import edu.uci.lighthouse.ui.views.actions.LayoutDropDownAction;
import edu.uci.lighthouse.ui.views.actions.LinkWithEditorAction;
import edu.uci.lighthouse.ui.views.actions.OpenInEditorAction;
import edu.uci.lighthouse.ui.views.actions.ZoomDropDownAction;

public class EmergingDesignView2 extends ThumbnailView implements IZoomableWorkbenchPart{
	
	private EditorListener editorListener = new EditorListener();
	private FilterManager filterManager;
	private static GraphViewer viewer = null;
	private static Logger logger = Logger.getLogger(EmergingDesignView2.class);
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		
		viewer = new GraphViewer(parent, SWT.NONE);
		//Create the FilterManager instance
		filterManager = new FilterManager(viewer);
		
		viewer.setContentProvider(new LighthouseRelationshipContentProvider());
		viewer.setLabelProvider(new LighthouseLabelProvider());
		viewer.setLayoutAlgorithm(new GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));		
		viewer.setInput(LighthouseModel.getInstance());
		

		
		//FIXME: Erase EditorListener and put everything in LinkWithEditorAction
		LinkWithEditorAction linkAction = new LinkWithEditorAction(viewer.getGraphControl());
		editorListener.addEditorSelectionListener(linkAction);
		
		getViewSite().getActionBars().getToolBarManager().add(linkAction);
		getViewSite().getActionBars().getToolBarManager().add(new HighlightDropDownAction(viewer.getGraphControl()));
		getViewSite().getActionBars().getToolBarManager().add(new LayoutDropDownAction(viewer.getGraphControl()));
		getViewSite().getActionBars().getToolBarManager().add(new DiagramModeDropDownAction(viewer.getGraphControl()));
		getViewSite().getActionBars().getToolBarManager().add(new FilterAuthorAction(viewer.getGraphControl()));
		getViewSite().getActionBars().getToolBarManager().add(new FilterModifiedAction(viewer.getGraphControl()));
		getViewSite().getActionBars().getToolBarManager().add(new ZoomDropDownAction(this));

		//FIXME: Change this to a decorator
		new OpenInEditorAction(viewer.getGraphControl());
		
		// Set the parameters for the thumbnail view
		setViewport(viewer.getGraphControl().getViewport());
		setSource(viewer.getGraphControl().getContents());
		setMainComposite(viewer.getGraphControl());
		
		getSite().getPage().addPartListener(editorListener);
	}

	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return viewer;
	}
	
	@Override
	public void dispose() {
		logger.info("dispose()");
		getSite().getPage().removePartListener(editorListener);
		super.dispose();
	}
}
