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
package edu.uci.lighthouse.ui.graph;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridLayout;

import edu.uci.lighthouse.ui.swt.util.ColorFactory;

public class UmlCompartmentFigure extends Figure {
	
	public UmlCompartmentFigure(){
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;		
		setLayoutManager(layout);	
	}

}
