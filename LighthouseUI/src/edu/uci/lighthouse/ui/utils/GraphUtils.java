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
package edu.uci.lighthouse.ui.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;

import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.ui.figures.AbstractUmlBoxFigure;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure;
import edu.uci.lighthouse.ui.views.EmergingDesignView;

public class GraphUtils {
	static EmergingDesignView view;
	static IViewPart viewPart;
	
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
	
	public static Collection<GraphNode> getSelectedGraphNodes(Graph graph) {
		LinkedList<GraphNode> result = new LinkedList<GraphNode>();
		for (Iterator itSelection = graph.getSelection()
				.iterator(); itSelection.hasNext();) {
			Object selection = itSelection.next();
			if (selection instanceof GraphNode) {
				result.add((GraphNode) selection);
			}
		}
		return result;
	}
	
	/**
	 * @author lee
	 */
	public static void rebuildFigureForEntity(final LighthouseEntity entity){
		Display.getDefault().asyncExec(new Runnable(){
			public void run() {
				GraphViewer viewer = getGraphViewer();
				System.out.println(viewer);
				GraphItem item = viewer.findGraphItem(entity);
				GraphUtils.rebuildFigure((GraphNode) item);
			}
		});
	}
	
	
	/**
	 * 
	 *@author lee
	 */
	public static GraphViewer getGraphViewer(){

		
		Display.getDefault().syncExec(new Runnable(){

			public void run() {
				IWorkbench workbench = PlatformUI.getWorkbench();
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				IWorkbenchPage page = window.getActivePage();
				viewPart = page.findView(EmergingDesignView.Plugin_ID);
				view = (EmergingDesignView)viewPart;
			}
			
		});


		return view.getGraphViewer();
	}
	
}
