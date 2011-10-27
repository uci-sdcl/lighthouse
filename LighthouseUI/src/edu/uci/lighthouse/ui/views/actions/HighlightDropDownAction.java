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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;

import edu.uci.lighthouse.ui.swt.util.ColorFactory;

public class HighlightDropDownAction extends Action implements IMenuCreator,  SelectionListener {

	private Menu menu;
	protected IContainer container;
	private List<IAction> actions;
	private Collection<GraphNode> selectedNodes;
	
	private static final String ICON = "$nl$/icons/elcl16/highlight.gif";
	private static final String DESCRIPTION = "Highlight";
	
	private static Logger logger = Logger.getLogger(HighlightDropDownAction.class);
	
	public HighlightDropDownAction(IContainer container){
		super(null,Action.AS_DROP_DOWN_MENU);
		this.container = container;
		init();
		actions = createActions();
		selectedNodes = new LinkedList<GraphNode>();
		setMenuCreator(this);
		//ISelectionChangedListener,ISelectionProvider
		container.getGraph().addSelectionListener(this);
	}

	private void init() {
		setToolTipText(DESCRIPTION);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.eclipse.help.ui", ICON));
	}
	
	private List<IAction> createActions() {
		List<IAction> result = new ArrayList<IAction>();
		result.add(new HighlightClassAction());
		result.add(new HighlightRelationshipsAction());
		return result;
	}

	private final class HighlightClassAction extends Action{
		public HighlightClassAction(){
			super("Highlight Class", Action.AS_CHECK_BOX);
		}
		@Override
		public void run() {
			if (isChecked()) {
				highlightNodes(container.getGraph().getSelection());
			} else {
				unhighlightNodes(container.getGraph().getSelection());
			}
		}
	}
	
	private final class HighlightRelationshipsAction extends Action{
		public HighlightRelationshipsAction(){
			super("Highlight Relationships", Action.AS_CHECK_BOX);
		}
		@Override
		public void run() {
			if (isChecked()) {
				highlightConnections(selectedNodes);
			} else {
				unhighlightConnections(selectedNodes);
			}
		}
	}
	
	private void updateFigures(Collection nodes, boolean highlight) {
		for (Iterator itNodes = nodes.iterator(); itNodes.hasNext();) {
			GraphNode node = (GraphNode) itNodes.next();
			IFigure figure = node.getNodeFigure();
			if (highlight) {
				figure.setBackgroundColor(ColorFactory.classHighlight);
				logger.debug("highlight: "+node);
			} else {
				figure.setBackgroundColor(ColorFactory.classBackground);
				logger.debug("unhighlight: "+node);
			}
		}
	}
	
	private void updateConnections(Collection relationships, boolean highlight){
		for (Iterator itConnections = relationships.iterator(); itConnections.hasNext();) {
			GraphConnection connection = (GraphConnection) itConnections.next();
			if (highlight){
				connection.highlight();
			} else {
				connection.unhighlight();
			}
		}
	}
	
	private void unhighlightNodes(Collection nodes){
		updateFigures(nodes,false);
	}
	
	private void highlightNodes(Collection nodes){
		updateFigures(nodes,true);
	}
	
	private void highlightConnections(Collection nodes){
		for (Iterator itNodes = nodes.iterator(); itNodes.hasNext();) {
			GraphNode node = (GraphNode) itNodes.next();
			updateConnections(node.getSourceConnections(), true);
			updateConnections(node.getTargetConnections(), true);
		}
	}
	
	private void unhighlightConnections(Collection nodes){
		for (Iterator itNodes = nodes.iterator(); itNodes.hasNext();) {
			GraphNode node = (GraphNode) itNodes.next();
			updateConnections(node.getSourceConnections(), false);
			updateConnections(node.getTargetConnections(), false);
		}
	}
	
	@Override
	public void dispose() {
		logger.debug("dispose()");
		if (menu != null){
			menu.dispose();
			menu = null;
		}
		container.getGraph().removeSelectionListener(this);
	}

	@Override
	public Menu getMenu(Control parent) {
		if (menu == null) {
			menu = new Menu(parent);
			logger.debug("menu instance created.");
			for (IAction layoutAction : actions) {
				ActionContributionItem item = new ActionContributionItem(
						layoutAction);
				item.fill(menu, -1);
			}
		}
		return menu;
	}

	@Override
	public Menu getMenu(Menu parent) {
		return null;
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		logger.debug("widgetSelected: "+e.item);
		unhighlightNodes(selectedNodes);
		unhighlightConnections(selectedNodes);
		selectedNodes.clear();
		selectedNodes.addAll(container.getGraph().getSelection());
		if (e.item instanceof GraphNode){
			for (IAction action : actions) {
				action.run();
			}
		}
	}
}
