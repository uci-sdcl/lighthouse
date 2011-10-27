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

import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Color;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.GraphNode;

import edu.uci.lighthouse.ui.swt.util.ColorFactory;

/**
 * Responsible to control the highlight mechanism in the viewer. Highlight
 * occurs everytime that the user clicks in any element in the viewer.
 * 
 * @author tproenca
 * 
 */
public class HighlightManager {

	private HashMap<GraphNode, TreeSet<IHightlightAction>> nodeToActions = new HashMap<GraphNode, TreeSet<IHightlightAction>>();
	private GraphViewer viewer;

	public HighlightManager(GraphViewer viewer) {
		this.viewer = viewer;
	}

	public GraphViewer getViewer() {
		return viewer;
	}

	public void add(IHightlightAction action, GraphNode node) {
		TreeSet<IHightlightAction> list = nodeToActions.get(node);
		if (list == null) {
			list = new TreeSet<IHightlightAction>(new Comparator<IHightlightAction>(){
				@Override
				public int compare(IHightlightAction o1, IHightlightAction o2) {
					if (o1.getPriority() == o2.getPriority()) {
						return 0;
					}
					return o2.getPriority() > o1.getPriority() ? 1 : -1 ;
				}});
			nodeToActions.put(node, list);
		}
		list.add(action);
		hightlight(node, action.getColor());
	}

	public void remove(IHightlightAction action, GraphNode node) {
		TreeSet<IHightlightAction> list = nodeToActions.get(node);
		if (list != null) {
			if(list.remove(action)){
				update(node);
			}
		}
	}
	
	private void update(GraphNode node){
		TreeSet<IHightlightAction> list = nodeToActions.get(node);
		if (list != null && list.size() > 0) {
			IHightlightAction action = list.last();
			hightlight(node, action.getColor());
		} else {
			hightlight(node, ColorFactory.classBackground);
		}
	}
	
	private void hightlight(GraphNode node, Color color) {
		node.setBackgroundColor(color);
	}

}
