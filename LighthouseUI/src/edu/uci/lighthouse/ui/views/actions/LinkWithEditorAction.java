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

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;

import edu.uci.lighthouse.ui.LighthouseUIPlugin;
import edu.uci.lighthouse.ui.swt.util.ColorFactory;
import edu.uci.lighthouse.ui.views.IEditorSelectionListener;

public class LinkWithEditorAction extends Action implements
		IEditorSelectionListener {

	protected IContainer container;
	private IFile selectedFile;

	private static final String ICON = "/icons/synced.gif";
	private static final String DESCRIPTION = "Link with editor";

	private static Logger logger = Logger.getLogger(LinkWithEditorAction.class);

	public LinkWithEditorAction(IContainer container) {
		super(null, IAction.AS_CHECK_BOX);
		this.container = container;
		init();
	}

	private void init() {
		setToolTipText(DESCRIPTION);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				LighthouseUIPlugin.PLUGIN_ID, ICON));
	}

	@Override
	public void run() {
		logger.info("run...");
		if (isChecked()) {
			highlightClassInDiagram(selectedFile);
		} else {
			unhighlightAll();
		}
	}

	private void highlightClassInDiagram(String classShortName) {
		logger.debug("shortName: " + classShortName);
		for (Iterator itNodes = container.getNodes().iterator(); itNodes
				.hasNext();) {
			GraphNode node = (GraphNode) itNodes.next();
			if (!isSelected(node)) {
				if (classShortName.equals(node.getText())) {
					node.setBackgroundColor(ColorFactory.classLinkWithEditor);
				} else {
					node.setBackgroundColor(ColorFactory.classBackground);
				}
			}
		}
	}

	private void highlightClassInDiagram(IFile classFile) {
		highlightClassInDiagram(classFile.getName().replaceAll(".java", ""));
	}

	private void unhighlightAll() {
		highlightClassInDiagram("");
	}
	
	private boolean isSelected(GraphNode node){
		logger.info("isSelected: "+container.getGraph().getSelection().indexOf(node) != -1+" ("+node.isSelected()+"");
		return container.getGraph().getSelection().indexOf(node) != -1;
	}

	@Override
	public void selectionChanged(IFile editedFile) {
		if (isChecked()) {
			highlightClassInDiagram(editedFile);
		}
		selectedFile = editedFile;
	}

}
