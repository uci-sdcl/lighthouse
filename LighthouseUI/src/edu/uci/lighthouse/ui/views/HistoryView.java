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
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;

import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.ui.swt.util.ColorFactory;

public class HistoryView extends ViewPart {

	private static GraphViewer viewer = null;
	private static Logger logger = Logger.getLogger(HistoryView.class);
	
	@Override
	public void createPartControl(Composite parent) {
		FormData data;		
		parent.setLayout(new FormLayout());			
		
		//---
		data = new FormData();
		data.top = new FormAttachment(0,0);
		data.left = new FormAttachment(0,0);
		data.right = new FormAttachment(100,0);
		data.bottom = new FormAttachment(100,-100);
		
		viewer = new GraphViewer(parent, SWT.NONE);
		viewer.setContentProvider(new LighthouseEntityContentProvider());
		viewer.setLabelProvider(new LighthouseLabelProvider());
		viewer.setLayoutAlgorithm(new GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));		
		viewer.setInput(LighthouseModel.getInstance());
		viewer.getControl().setLayoutData(data);
		

		//---
		data = new FormData();
		data.top = new FormAttachment(viewer.getControl(),0);
		data.left = new FormAttachment(0,0);
		data.right = new FormAttachment(100,0);
		data.bottom = new FormAttachment(100,0);
		
		ScrolledComposite horizontalComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		horizontalComposite.setExpandHorizontal(true);
		horizontalComposite.setExpandVertical(true);
		horizontalComposite.setLayoutData(data);

		
		Composite historyComposite = new Composite(horizontalComposite, SWT.NONE);
		historyComposite.setBackground(ColorConstants.gray);
		
		horizontalComposite.setContent(historyComposite);
		horizontalComposite.setMinSize(historyComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	@Override
	public void setFocus() {
		viewer.getGraphControl().setFocus();
	}

}
