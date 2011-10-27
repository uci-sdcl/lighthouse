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
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;

import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.ui.LighthouseUIPlugin;
import edu.uci.lighthouse.ui.swt.util.ColorFactory;
import edu.uci.lighthouse.ui.views.IEditorSelectionListener;

public class LinkWithEditorAction extends Action implements
		IEditorSelectionListener {

	// protected IContainer container;
	private GraphViewer viewer;
	private IFile selectedFile;
	private GraphNode lastHightlightedNode;

	private static final String ICON = "/icons/synced.gif";
	private static final String DESCRIPTION = "Link with editor";

	private static Logger logger = Logger.getLogger(LinkWithEditorAction.class);

	public LinkWithEditorAction(GraphViewer viewer) {
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
		logger.info("run...");
		if (isChecked()) {
			highlightClassInDiagram(selectedFile);
		} else {
//			unhighlightAll();
			if (!lastHightlightedNode.isDisposed()) {
				lastHightlightedNode.setBackgroundColor(ColorFactory.classBackground);
			}
			lastHightlightedNode = null;
		}
	}

	@Deprecated
	private void highlightClassInDiagram(String classShortName) {
		logger.debug("shortName: " + classShortName);
		for (Iterator itNodes = viewer.getGraphControl().getNodes().iterator(); itNodes
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
		// highlightClassInDiagram(classFile.getName().replaceAll(".java", ""));
		IJavaElement jFile = JavaCore.create(classFile);
		if (jFile instanceof ICompilationUnit) {
			IType type = ((ICompilationUnit) jFile).findPrimaryType();
			String fqn = jFile.getJavaProject().getElementName() + "."
					+ type.getFullyQualifiedName();
			LighthouseEntity entity = LighthouseModel.getInstance().getEntity(
					fqn);
			if (entity != null) {
				GraphItem gItem = viewer.findGraphItem(entity);
				if (gItem instanceof GraphNode
						&& !gItem.equals(lastHightlightedNode)) {
					GraphNode node = (GraphNode) gItem;
					node.setBackgroundColor(ColorFactory.classLinkWithEditor);
					if (lastHightlightedNode != null && !lastHightlightedNode.isDisposed()) {
						lastHightlightedNode
								.setBackgroundColor(ColorFactory.classBackground);
					}
					lastHightlightedNode = node;
				}
			}
		}
	}

	@Deprecated
	private void unhighlightAll() {
		highlightClassInDiagram("");
	}

	private boolean isSelected(GraphNode node) {
		logger.info("isSelected: "
				+ String.valueOf(viewer.getGraphControl().getSelection().indexOf(
						node) != -1) + " (" + node.isSelected() + ")");
		return viewer.getGraphControl().getSelection().indexOf(node) != -1;
	}

	@Override
	public void selectionChanged(IFile editedFile) {
		logger.info("selectionChanged");
		if (isChecked()) {
			highlightClassInDiagram(editedFile);
		}
		selectedFile = editedFile;
	}

	@Override
	public void editorClosed() {
		// TODO Auto-generated method stub
		
	}

}
