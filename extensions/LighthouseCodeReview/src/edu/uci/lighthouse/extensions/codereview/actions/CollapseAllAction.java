package edu.uci.lighthouse.extensions.codereview.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class CollapseAllAction extends Action {
	private static final String ICON = "$nl$/icons/elcl16/collapseall.gif";
	private static final String DESCRIPTION = "Collapse all";
	private TreeViewer viewer;
	
	public CollapseAllAction(TreeViewer viewer){
		this.viewer = viewer;
		init();
	}

	private void init() {
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.eclipse.ui.cheatsheets", ICON));
		setText(DESCRIPTION);
		setToolTipText(DESCRIPTION);
	}
	
	@Override
	public void run() {
		viewer.collapseAll();
	}
}
