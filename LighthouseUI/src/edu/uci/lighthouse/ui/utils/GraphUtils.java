package edu.uci.lighthouse.ui.utils;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.zest.core.widgets.GraphNode;

import edu.uci.lighthouse.ui.figures.AbstractUmlBoxFigure;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure;
import edu.uci.lighthouse.ui.figures.UmlClassFigure;

public class GraphUtils {

	public static void changeFigureMode(GraphNode node, ILighthouseClassFigure.MODE mode){
		Point loc = node.getLocation();
		Dimension size = new Dimension(-1,-1);
		Rectangle bounds = new Rectangle(loc, size);
		Assert.isLegal(node.getNodeFigure() instanceof ILighthouseClassFigure, "Invalid IFigure found, only "+ILighthouseClassFigure.class.getSimpleName()+" is supported for change the mode.");
		AbstractUmlBoxFigure fig = (AbstractUmlBoxFigure) node.getNodeFigure();
		fig.getParent().setConstraint(fig,bounds);		
		fig.populate(mode);
	}
	
	public static void rebuildFigure(GraphNode node){
		Assert.isLegal(node.getNodeFigure() instanceof ILighthouseClassFigure, "Invalid IFigure found, only "+ILighthouseClassFigure.class.getSimpleName()+" is supported for rebuild.");
		changeFigureMode(node,((ILighthouseClassFigure)node.getNodeFigure()).getCurrentLevel());
	}
}
