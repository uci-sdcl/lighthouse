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
package edu.uci.lighthouse.ui.views.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;

import edu.uci.lighthouse.ui.LighthouseUIPlugin;
import edu.uci.lighthouse.ui.swt.util.ColorFactory;



public class SmellsAction extends Action {
	
	private GraphViewer viewer;

	private static final Color COLOR_PROBLEM = ColorFactory.red;
	private static final Color COLOR_DEFAULT = ColorConstants.lightGray;
	
	private static final String ICON = "/icons/synced.gif";
	private static final String DESCRIPTION = "Highlights possible design flaws";

	public SmellsAction(GraphViewer viewer) {
		super(null, IAction.AS_CHECK_BOX);
		this.viewer = viewer;
		init();
	}
	
	private void init() {
		setToolTipText(DESCRIPTION);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				LighthouseUIPlugin.PLUGIN_ID, ICON));
	}
	
	@Override
	public void run() {
		if (isChecked()) {
			hightlightBidirecionalRelationships();
		} else {
			unhightlightRelationships();
		}
	}
	
	private void hightlightBidirecionalRelationships() {
		Collection<GraphConnection> relationships = getBidirecionalRelationships();
		for (GraphConnection graphConnection : relationships) {
			graphConnection.setLineColor(COLOR_PROBLEM);
		}
	}
	
	private void unhightlightRelationships() {
		 List connections = viewer.getGraphControl().getConnections();
		 for (Iterator itConnections = connections.iterator(); itConnections.hasNext();) {
			GraphConnection connection = (GraphConnection) itConnections.next();
			connection.setLineColor(COLOR_DEFAULT);
		}
	}
	
	private Collection<GraphConnection> getBidirecionalRelationships() {
		Collection<GraphConnection> result = new LinkedList<GraphConnection>();
		ConnectionsMap cache = new ConnectionsMap();
		 List connections = viewer.getGraphControl().getConnections();
		 for (Iterator itConnections = connections.iterator(); itConnections.hasNext();) {
			GraphConnection connection = (GraphConnection) itConnections.next();
			if (cache.contains(connection)) {
				result.add(connection);
			}
			cache.add(connection);
		}
		return result;
	}
	
	private final class ConnectionsMap {
		private HashMap<GraphNode, LinkedHashSet<GraphNode>> map = new HashMap<GraphNode, LinkedHashSet<GraphNode>>();
		public boolean contains(GraphConnection connection) {
			LinkedHashSet<GraphNode> list = map.get(connection.getSource());
			return list == null ? false : list.contains(connection.getDestination());
		}
		public void add(GraphConnection connection) {
			add(connection.getSource(), connection.getDestination());
			add(connection.getDestination(), connection.getSource());
		}
		public void getReverse(GraphConnection connection) {
			LinkedHashSet<GraphNode> list = map.get(connection.getDestination());
			for (Iterator itNodes = list.iterator(); itNodes.hasNext();) {
				GraphNode graphNode = (GraphNode) itNodes.next();
				if (graphNode.equals(connection.getSource())) {
					
				}
			}
		}
		private void add(GraphNode source, GraphNode destination) {
			LinkedHashSet<GraphNode> list = map.get(source);
			 if (list == null) {
				 list = new LinkedHashSet<GraphNode>();
				 map.put(source, list);
			 }
			list.add(destination);
		}
	}
	
//	private GraphNode from;
//	private GraphNode to;
//	
//	public ConnectionEntry(GraphNode from, GraphNode to) {
//		this.from = from;
//		this.to = to;
//	}
//	
//	@Override
//	public boolean equals(Object obj) {
//		if (obj instanceof ConnectionEntry) {
//			ConnectionEntry otherObj = (ConnectionEntry) obj;
//			if ((this.from.equals(otherObj.from) && this.to.equals(otherObj.to)) || 
//				(this.from.equals(otherObj.to) && this.to.equals(otherObj.from))) {
//				return true;
//			}
//		}
//		return false;
//	}
}
