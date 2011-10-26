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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;

import edu.uci.lighthouse.ui.swt.util.ColorFactory;
import edu.uci.lighthouse.ui.utils.GraphUtils;
import edu.uci.lighthouse.ui.views.HighlightManager;
import edu.uci.lighthouse.ui.views.IContextMenuAction;
import edu.uci.lighthouse.ui.views.IHightlightAction;

public class SoftLockAction extends Action implements IHightlightAction, IContextMenuAction {

	protected IContainer container;
	private static Logger logger = Logger.getLogger(SoftLockAction.class);
	private static final String ICON = "$nl$/icons/full/elcl16/deadlock_view.gif";
	private static final String ADD_SOFT_LOCK = "Add to soft lock list";
	private static final String REMOVE_SOFT_LOCK = "Remove from soft lock list";
	private LinkedHashSet<GraphNode> lockedNodes = new LinkedHashSet<GraphNode>();
	private HighlightManager manager;
	
	public SoftLockAction(HighlightManager manager) {
		super(null,Action.AS_PUSH_BUTTON);
		this.container = manager.getViewer().getGraphControl();
		this.manager = manager;
		init();
	}

	private void init() {
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.eclipse.jdt.debug.ui", ICON));
	}

	public void beforeFill() {
		// Updates the action label.
		String message = ADD_SOFT_LOCK;
		Collection<GraphNode> selectedNodes = GraphUtils.getSelectedGraphNodes((Graph) container);
		for(GraphNode node: selectedNodes){
			if(lockedNodes.contains(node)){
				message = REMOVE_SOFT_LOCK;
				break;
			} 
		}
		setText(message);
	}

	@Override
	public void run() {
		logger.debug("locked Nodes: "+lockedNodes);
		Collection<GraphNode> selectedNodes = GraphUtils.getSelectedGraphNodes((Graph) container);
		for(GraphNode node: selectedNodes){
			if(lockedNodes.contains(node)){
				// User wants to remove the lock
				lockedNodes.remove(node);
				manager.remove(this, node);
			} else {
				// Add the lock
				lockedNodes.add(node);
				manager.add(this, node);
			}
		}
	}

	@Override
	public int getPriority() {
		return IHightlightAction.SOFT_LOCK;
	}

	@Override
	public Color getColor() {
		return ColorFactory.classSoftLock;
	}
}
