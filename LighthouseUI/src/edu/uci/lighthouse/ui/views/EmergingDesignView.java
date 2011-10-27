/*******************************************************************************
* Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
*				, University of California, Irvine}.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    {Software Design and Collaboration Laboratory (SDCL)
*	, University of California, Irvine}
*			- initial API and implementation and/or initial documentation
*******************************************************************************/
package edu.uci.lighthouse.ui.views;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;

import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.ui.views.actions.BookmarkFilterDropDownAction;
import edu.uci.lighthouse.ui.views.actions.DiagramModeDropDownAction;
import edu.uci.lighthouse.ui.views.actions.FilterActiveClassAction;
import edu.uci.lighthouse.ui.views.actions.FilterAuthorAction;
import edu.uci.lighthouse.ui.views.actions.FilterModifiedAction;
import edu.uci.lighthouse.ui.views.actions.FilterOpenEditorAction;
import edu.uci.lighthouse.ui.views.actions.FilterPackageAction;
import edu.uci.lighthouse.ui.views.actions.HighlightDropDownAction;
import edu.uci.lighthouse.ui.views.actions.LayoutDropDownAction;
import edu.uci.lighthouse.ui.views.actions.LinkWithEditorAction;
import edu.uci.lighthouse.ui.views.actions.OpenInEditorAction;
import edu.uci.lighthouse.ui.views.actions.SmellsAction;
import edu.uci.lighthouse.ui.views.actions.ZoomDropDownAction;

public class EmergingDesignView extends ThumbnailView implements IZoomableWorkbenchPart{
	
	private EditorListener editorListener = new EditorListener();
	private FilterManager filterManager;
	private static GraphViewer viewer = null;
	private static Logger logger = Logger.getLogger(EmergingDesignView.class);
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		
		viewer = new GraphViewer(parent, SWT.NONE);
		viewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
		//Create the FilterManager instance
		filterManager = new FilterManager(viewer);
		
//		viewer.setContentProvider(new LighthouseRelationshipContentProvider());
		viewer.setContentProvider(new LighthouseEntityContentProvider());
		viewer.setLabelProvider(new LighthouseLabelProvider());
		viewer.setLayoutAlgorithm(new GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));		
		viewer.setInput(LighthouseModel.getInstance());
		
		
		
		//FIXME: Erase EditorListener and put everything in LinkWithEditorAction
		LinkWithEditorAction linkAction = new LinkWithEditorAction(viewer);
		FilterActiveClassAction activeClassAction = new FilterActiveClassAction(viewer);
//		FilterOpenEditorAction openEditorAction = new FilterOpenEditorAction(viewer);
		editorListener.addEditorSelectionListener(activeClassAction);
//		editorListener.addEditorSelectionListener(openEditorAction);
		editorListener.addEditorSelectionListener(linkAction);
		
//		getViewSite().getActionBars().getToolBarManager().add(linkAction);
		getViewSite().getActionBars().getToolBarManager().add(new HighlightDropDownAction(viewer.getGraphControl()));
		getViewSite().getActionBars().getToolBarManager().add(new LayoutDropDownAction(viewer.getGraphControl()));
		getViewSite().getActionBars().getToolBarManager().add(new DiagramModeDropDownAction(viewer.getGraphControl()));
		getViewSite().getActionBars().getToolBarManager().add(new FilterAuthorAction(viewer.getGraphControl()));
		getViewSite().getActionBars().getToolBarManager().add(new FilterPackageAction(viewer));
		getViewSite().getActionBars().getToolBarManager().add(new FilterModifiedAction(viewer.getGraphControl()));
		getViewSite().getActionBars().getToolBarManager().add(activeClassAction);
//		getViewSite().getActionBars().getToolBarManager().add(openEditorAction);
//		getViewSite().getActionBars().getToolBarManager().add(new BookmarkFilterDropDownAction(viewer.getGraphControl()));
//		getViewSite().getActionBars().getToolBarManager().add(new SmellsAction(viewer));
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
