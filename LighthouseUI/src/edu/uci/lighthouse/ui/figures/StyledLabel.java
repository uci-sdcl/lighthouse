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

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

public class StyledLabel extends Label {
	
	private boolean strikeout = false;
	
	public StyledLabel(String s, Image i){
		super(s, i);
	}
	
	/**
	 * @return the strikeout
	 */
	public boolean isStrikeout() {
		return strikeout;
	}

	/**
	 * @param strikeout the strikeout to set
	 */
	public void setStrikeout(boolean strikeout) {
		this.strikeout = strikeout;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paint(org.eclipse.draw2d.Graphics)
	 */
	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		if (strikeout){
			final Rectangle bounds = getBounds();
			int y = bounds.y + bounds.height/2;
			graphics.drawLine(bounds.x,y,bounds.right(),y);
		}
	}
}
