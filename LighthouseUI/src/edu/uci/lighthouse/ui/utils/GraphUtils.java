package edu.uci.lighthouse.ui.utils;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.zest.core.widgets.GraphNode;

import edu.uci.lighthouse.ui.graph.IUmlClass;
import edu.uci.lighthouse.ui.graph.UmlClassFigureTest;

public class GraphUtils {

	public static void changeFigureMode(GraphNode node, IUmlClass.LEVEL level){
		Point loc = node.getLocation();
		Dimension size = new Dimension(-1,-1);
		Rectangle bounds = new Rectangle(loc, size);
		Assert.isLegal(node.getNodeFigure() instanceof UmlClassFigureTest, "Invalid IFigure found, only "+UmlClassFigureTest.class.getSimpleName()+" is supported for change the mode.");
		UmlClassFigureTest fig = (UmlClassFigureTest) node.getNodeFigure();
		fig.getParent().setConstraint(fig,bounds);		
		fig.populate(level);
	}
	
	public static void rebuildFigure(GraphNode node){
		Assert.isLegal(node.getNodeFigure() instanceof UmlClassFigureTest, "Invalid IFigure found, only "+UmlClassFigureTest.class.getSimpleName()+" is supported for rebuild.");
		changeFigureMode(node,((UmlClassFigureTest)node.getNodeFigure()).getCurrentLevel());
	}
}
