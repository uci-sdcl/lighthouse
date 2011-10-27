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
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.Animation;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;

import edu.uci.lighthouse.ui.LighthouseUIPlugin;
import edu.uci.lighthouse.ui.graph.IUmlClass;
import edu.uci.lighthouse.ui.utils.GraphUtils;

public class DiagramModeDropDownAction extends DropDownAction{

	private static Logger logger = Logger.getLogger(DiagramModeDropDownAction.class);
	
	private static final String ICON = "/icons/console.gif";
	private static final String DESCRIPTION = "Diagram Mode";

	public DiagramModeDropDownAction(IContainer container) {
		super(container);
	}
	
	@Override
	protected List<IAction> createActions() {
		List<IAction> result = new ArrayList<IAction>();
		result.add(new DiagramModeAction(IUmlClass.LEVEL.ONE));
		result.add(new DiagramModeAction(IUmlClass.LEVEL.TWO));
		result.add(new DiagramModeAction(IUmlClass.LEVEL.THREE));
		result.add(new DiagramModeAction(IUmlClass.LEVEL.FOUR));
		return result;
	}
	
	private final class DiagramModeAction extends Action {
		IUmlClass.LEVEL level;
		public DiagramModeAction(IUmlClass.LEVEL level){
			super("Mode " + (level.ordinal()+1),Action.AS_RADIO_BUTTON);
			this.level = level;			
		}
		@Override
		public void run() {
			if (isChecked()) {
				logger.debug(getText() + " running...");
				refreshAllFigures(level);
				selectedAction = this;
			}
		}
	}

	@Override
	protected int getDefaultActionIndex() {
		return 0;
	}

	@Override
	protected void init() {
		setToolTipText(DESCRIPTION);
		setImageDescriptor(AbstractUIPlugin
				.imageDescriptorFromPlugin(LighthouseUIPlugin.PLUGIN_ID, ICON));
	}
	
	private void refreshAllFigures(IUmlClass.LEVEL level){
		Animation.markBegin();
		for (Iterator itNodes = container.getNodes().iterator(); itNodes.hasNext();) {
			GraphNode node = (GraphNode) itNodes.next();
			GraphUtils.changeFigureMode(node,level);
/*			if (node instanceof UmlClassNode){	
				UmlClassNode classNode = (UmlClassNode) node;
				classNode.setLevel(level);
			}*/
		}
		Animation.run(150);
	}
}
