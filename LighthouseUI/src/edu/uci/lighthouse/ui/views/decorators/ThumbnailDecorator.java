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
package edu.uci.lighthouse.ui.views.decorators;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.widgets.IContainer;

public class ThumbnailDecorator extends ContainerDecorator {

	public ThumbnailDecorator(IContainer container) {
		super(container);
	}

	@Override
	protected void init(Composite parent) {
		FormData data;		
		//parent.setLayout(new FormLayout());
		parent.setLayout(new FillLayout());
		
		data = new FormData();
		data.top = new FormAttachment(100,-130);
		data.left = new FormAttachment(100,-130);
		data.right = new FormAttachment(100,-30);
		data.bottom = new FormAttachment(100,-30);
		
		FigureCanvas thumbnail = new FigureCanvas(parent, SWT.NONE);
		thumbnail.setBackground(ColorConstants.white);
		//thumbnail.setLayoutData(data);
		
		ScrollableThumbnail tb = new ScrollableThumbnail();
		tb.setBorder(new LineBorder(1));
		tb.setViewport(getGraph().getViewport());
		tb.setSource(getGraph().getContents());
		thumbnail.setContents(tb);
		
		data = new FormData();
		data.top = new FormAttachment(0,0);
		data.left = new FormAttachment(0,0);
		data.right = new FormAttachment(100,0);
		data.bottom = new FormAttachment(100,0);
		//getGraph().setLayoutData(data);	
	}
}
