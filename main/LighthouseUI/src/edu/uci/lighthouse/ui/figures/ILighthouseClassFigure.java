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

import org.eclipse.draw2d.IFigure;

import edu.uci.lighthouse.model.LighthouseEntity;

public interface ILighthouseClassFigure extends IFigure {
	/** Lighthouse diagram modes */
	public static enum MODE {ONE, TWO, THREE, FOUR};
	
	/** Populate the figure for the given mode */
	public void populate(MODE mode);
	
	/** Return the current diagram mode */
	public MODE getCurrentLevel();
	
	/** Returns the <code>LighthouseEntity</code> related with the class for the given position. */
	public LighthouseEntity findLighthouseEntityAt(int x, int y);
}
