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

import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;

public class MouseAdapter implements MouseListener{

	@Override
	public void mouseDoubleClicked(MouseEvent me) {
	}

	@Override
	public void mousePressed(MouseEvent me) {
		System.out.println("mousePressed");
	}

	@Override
	public void mouseReleased(MouseEvent me) {
	}

}
