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

import java.util.Iterator;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;

import edu.uci.lighthouse.ui.swt.util.ColorFactory;

public class UmlClassBorder extends AbstractBorder{

	private Color color;

	public UmlClassBorder(){
		
	}
	
	public UmlClassBorder(Color color){
		setColor(color);
	}
	
	@Override
	public Insets getInsets(IFigure figure) {
		//Insets(2,5,2,7);
		return new Insets(2,5,8,7);
	}

	@Override
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		if (figure instanceof AbstractUmlBoxFigure){
			// Get bounds
			tempRect.setBounds(getPaintRectangle(figure, insets));
			
			// Draw the shadow
			tempRect.resize(-2, -2);
			graphics.setLineWidth(3);
			graphics.setForegroundColor(ColorFactory.white);
			graphics.drawLine(tempRect.right(), tempRect.y, tempRect.right(), tempRect.bottom() -1);
			graphics.setForegroundColor(ColorFactory.lightGray);
			graphics.drawLine(tempRect.x + 4, tempRect.bottom(), tempRect.right()+2, tempRect.bottom());
			graphics.drawLine(tempRect.right(), tempRect.y + 4 , tempRect.right(), tempRect.bottom()+2);

			graphics.restoreState();
			AbstractUmlBoxFigure boxFigure = (AbstractUmlBoxFigure) figure;
			
			// Draw the events separator
			graphics.setForegroundColor(ColorFactory.classEventSeparator);		
			for (IFigure fig : boxFigure.getSeparatorEvents()) {
				int y = fig.getBounds().y;
				graphics.drawLine(tempRect.x,y,tempRect.right()-1,y);
			}
			
			// Draw the border and block separators
			if (getColor() != null)
				graphics.setForegroundColor(getColor());
			tempRect.resize(-1, -1);
			for (IFigure fig : boxFigure.getSeparators()) {
				int y = fig.getBounds().y + (fig.getBounds().height/2);
				graphics.drawLine(tempRect.x,y,tempRect.right(),y);
			}
			graphics.drawRectangle(tempRect);			
		}
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

}
