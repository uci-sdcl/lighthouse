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
package edu.uci.lighthouse.ui.figures;

import org.eclipse.draw2d.Panel;

import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure.MODE;

public abstract class CompartmentFigure extends Panel {

	private LighthouseEntity umlClass;
	
	/** Indicates if the figure should be shown for the given mode */
	public abstract boolean isVisible(MODE mode);
	
	/** Populate the figure for the given mode */
	public abstract void populate(MODE mode);
	
	protected void setUmlClass(LighthouseEntity umlClass) {
		this.umlClass = umlClass;
	}

	protected LighthouseEntity getUmlClass() {
		return umlClass;
	}
	
}
