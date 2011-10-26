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

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;

import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.ui.LighthouseUIPlugin;
import edu.uci.lighthouse.ui.swt.util.ColorFactory;
import edu.uci.lighthouse.ui.views.HighlightManager;
import edu.uci.lighthouse.ui.views.IEditorSelectionListener;
import edu.uci.lighthouse.ui.views.IHightlightAction;

public class LinkWithEditorAction extends Action implements
		IEditorSelectionListener, IHightlightAction {

	private GraphViewer viewer;
	/** Caches the current selected file in the editor */
	private IFile selectedFile;
	/** Caches the current highlighted node */
	private GraphNode currentHighlightedNode;
	private HighlightManager manager;

	private static final String ICON = "/icons/synced.gif";
	private static final String DESCRIPTION = "Highlight the active class";

	private static Logger logger = Logger.getLogger(LinkWithEditorAction.class);

	public LinkWithEditorAction(HighlightManager manager) {
		super(null, IAction.AS_CHECK_BOX);
		this.viewer = manager.getViewer();
		this.manager = manager;
		init();
		setChecked(true);
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
			unHighlightNode(currentHighlightedNode);
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
						&& !gItem.equals(currentHighlightedNode)) {
					unHighlightNode(currentHighlightedNode);
					highlightNode((GraphNode) gItem);
				}
			}
		}
	}

	private void highlightNode(GraphNode node) {
		if (node != null && !node.isDisposed()) {
			manager.add(this, node);
			currentHighlightedNode = node;
		}
	}

	private void unHighlightNode(GraphNode node) {
		if (node != null && !node.isDisposed()) {
			manager.remove(this, node);
			currentHighlightedNode = null;
		}
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

	@Override
	public int getPriority() {
		return IHightlightAction.LINK_WITH_EDITOR;
	}

	@Override
	public Color getColor() {
		return ColorFactory.classLinkWithEditor;
	}

}
