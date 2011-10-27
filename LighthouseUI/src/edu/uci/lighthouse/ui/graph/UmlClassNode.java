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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;

import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseField;
import edu.uci.lighthouse.model.LighthouseMethod;
import edu.uci.lighthouse.ui.swt.util.ColorFactory;

public class UmlClassNode extends GraphNode implements IUmlClass{

	private List<LighthouseField> fields = new LinkedList<LighthouseField>();
	private List<LighthouseMethod> methods = new LinkedList<LighthouseMethod>();
	private LEVEL currentLevel = LEVEL.ONE;
	private LighthouseClass c;
	
	public UmlClassNode(IContainer graphModel, int style, LighthouseClass aClass) {
		super(graphModel, style, aClass.getShortName(), aClass);
		c = aClass;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.zest.core.widgets.GraphNode#createFigureForModel()
	 */
	@Override
	protected IFigure createFigureForModel() {
		IFigure fig = new UmlClassFigure(this);
		//TODO: Return it when classChanged() is working
		//fig.addLayoutListener(LayoutAnimator.getDefault()); 
		fig.setSize(-1, -1);
		return fig;
	}

	@Override
	public Collection<LighthouseField> getFields() {
		return fields;
	}

	@Override
	public Collection<LighthouseMethod> getMethods() {
		return methods;
	}

	public void addMethod(LighthouseMethod m){
		methods.add(m);
	}
	
	public void addField(LighthouseField f){
		fields.add(f);
	}

	@Override
	protected void updateFigureForModel(IFigure figure) {
		if (figure != null) {
			if (highlighted == HIGHLIGHT_ON) {
				figure.setForegroundColor(getForegroundColor());
				figure.setBackgroundColor(getHighlightColor());
				// figure.setBorderColor(getBorderHighlightColor());
			} else {
				figure.setForegroundColor(getForegroundColor());
				figure.setBackgroundColor(getBackgroundColor());
				// figure.setBorderColor(getBorderColor());
			}
		}
	}

	@Override
	protected void initModel(IContainer parent, String text, Image image) {
		super.initModel(parent, text, image);
		setBackgroundColor(ColorFactory.white);
		setForegroundColor(ColorFactory.black);
		setHighlightColor(ColorFactory.tooltipBackground);
	}

	public void setLevel(LEVEL level){
		Point loc = getLocation();
		Dimension size = new Dimension(-1,-1);
		Rectangle bounds = new Rectangle(loc, size);
		UmlClassFigure fig = (UmlClassFigure)getNodeFigure();
		fig.getParent().setConstraint(fig,bounds);		
		fig.populate(level);
		currentLevel = level;
	}
	
	public void rebuild(){
		setLevel(currentLevel);
	}
	
	public void clear(){
		fields.clear();
		methods.clear();
	}

	public LighthouseClass getLighthouseClass() {
		return c;
	}
}
