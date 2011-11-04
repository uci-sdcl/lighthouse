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
package edu.uci.lighthouse.ui.views.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.viewers.GraphViewer;

import edu.uci.lighthouse.ui.views.FilterManager;
import edu.uci.lighthouse.ui.views.IEditorSelectionListener;
import edu.uci.lighthouse.views.filters.OpenEditorFilter;

public class FilterOpenEditorAction extends Action implements IEditorSelectionListener{

	//FIXME: Not using container (graph or viewer)
	protected GraphViewer viewer;
	
	private static final String ICON = "$nl$/icons/full/obj16/elements_obj.gif";
	private static final String DESCRIPTION = "Show classes opened in the editor and its dependencies";

	private static Logger logger = Logger.getLogger(FilterOpenEditorAction.class);
	
	OpenEditorFilter filter = new OpenEditorFilter();
	
	public FilterOpenEditorAction(GraphViewer viewer){
		super(null, IAction.AS_CHECK_BOX);
		this.viewer = viewer;
		init();
	}
	
	private void init() {
		setToolTipText(DESCRIPTION);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.eclipse.ui", ICON));
	}
	
	@Override
	public void run() {
		if (isChecked()){
			FilterManager.getInstance().addViewerFilter(filter);
			viewer.applyLayout();
		} else {
			FilterManager.getInstance().removeViewerFilter(filter);
		}
	}

	@Override
	public void selectionChanged(IFile editedFile) {
		logger.info("selectionChanged");
		if (isChecked()) {
			viewer.refresh();
			viewer.applyLayout();
		}
	}

	@Override
	public void editorClosed() {
		viewer.refresh();
	}

}
