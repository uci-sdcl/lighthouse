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

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IFigureProvider;

import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseInterface;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.ui.figures.InterfaceFigure;
import edu.uci.lighthouse.ui.figures.UmlClassFigure;

public class LighthouseLabelProvider extends LabelProvider implements IFigureProvider {

	@Override
	public IFigure getFigure(Object element) {
		if (element instanceof LighthouseClass){
			IFigure fig = new UmlClassFigure((LighthouseClass)element);
			fig.setSize(-1,-1);
			return fig;
			//return new Label(element.toString());
		} if (element instanceof LighthouseInterface) {
			IFigure fig = new InterfaceFigure((LighthouseInterface)element);
			fig.setSize(-1,-1);
			return fig;
		}
		//System.out.println(element.toString());
		return null;
		//return null;
	}

	@Override
	public String getText(Object element) {
		if ((element instanceof EntityConnectionData)||element instanceof LighthouseRelationship){
			return "";
		} else if (element instanceof LighthouseEntity){
			return ((LighthouseEntity) element).getShortName();
		}
		return super.getText(element);
	}
}
