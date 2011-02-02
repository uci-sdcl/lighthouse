package edu.uci.lighthouse.ui.views.actions;

import java.util.Iterator;
import java.util.LinkedHashSet;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;

import edu.uci.lighthouse.ui.swt.util.ColorFactory;

public class SoftLockAction extends Action {

	protected IContainer container;
	private static Logger logger = Logger.getLogger(SoftLockAction.class);
	private static final String ICON = "$nl$/icons/full/elcl16/deadlock_view.gif";
	private static final String ADD_SOFT_LOCK = "Add to soft lock list";
	private static final String REMOVE_SOFT_LOCK = "Remove to soft lock list";
	private static LinkedHashSet<GraphNode> lockedNodes = new LinkedHashSet<GraphNode>(); 
	
	public SoftLockAction(IContainer container) {
		super(null,Action.AS_CHECK_BOX);
		this.container = container;
		init();
	}

	private void init() {
		setText(ADD_SOFT_LOCK);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.eclipse.jdt.debug.ui", ICON));
	}

	@Override
	public void run() {
		Graph graph = (Graph) container;
		
		for(Iterator i = graph.getSelection().iterator(); i.hasNext();){
			GraphNode node = (GraphNode) i.next(); 
			if(lockedNodes.contains(node)){
				// User wants to remove the lock
				lockedNodes.remove(node);
				node.setBackgroundColor(ColorFactory.classBackground);
			} else {
				// Add the lock
				lockedNodes.add(node);
				node.setBackgroundColor(ColorFactory.classSoftLock);
			}
		}
		
		System.out.println("selectedNodes Array: " + lockedNodes); 
		

	}
	/*
	public ArrayList<GraphNode> addNewSelected(){
		for(Iterator i = getSelectedGraphNodes().iterator(); i.hasNext();){
			GraphNode current = (GraphNode) i.next(); 
			if (lockedNodes.contains(current)){
				lockedNodes.remove(current); 
				System.out.println(current + " has been unchecked"); 
			}
			else{
				
				lockedNodes.add(current); 
			}
		}
		
		return lockedNodes; 
	}
	
	@Override
	public boolean isChecked() {
		// TODO Auto-generated method stub
		return super.isChecked();
	}

	private void unhighlightNodes(Collection<GraphNode> nodes){
		updateFigures(nodes,false);
	}
	
	private void highlightNodes(Collection<GraphNode> nodes){
		updateFigures(nodes,true);
	}
	
	private void updateFigures(Collection<GraphNode> nodes, boolean highlight) {
		for (GraphNode node : nodes) {
			if (!isLinkedWithEditor(node)&&!node.isDisposed()) {
			if (highlight) {
				node.setBackgroundColor(ColorFactory.classSoftLock);
				//super.setChecked(true); 
				logger.debug("softlock: "+node);
			} else {
				node.setBackgroundColor(ColorFactory.classBackground);
				//super.setChecked(false);
				logger.debug("softlock: "+node);
			}
			}
		}
	}
	
	private void test(Collection<GraphNode> nodes) {
		for (GraphNode node : nodes) {
			if (!isLinkedWithEditor(node)&&!node.isDisposed()) {
			if (lockedNodes.contains(node)){// (highlight) {
				node.setBackgroundColor(ColorFactory.classSoftLock);
				super.setChecked(true); 
				logger.debug("softlock: "+node);
			} else {
				node.setBackgroundColor(ColorFactory.classBackground);
				super.setChecked(false);
				logger.debug("softlock: "+node);
			}
			}
		}
	}
	
	private boolean isLinkedWithEditor(GraphNode node){
		return node.getBackgroundColor().equals(ColorFactory.classLinkWithEditor);
	}
	
	public Collection<GraphNode> getSelectedGraphNodes(){
		LinkedList<GraphNode> result = new LinkedList<GraphNode>();
		for (Iterator itSelection = container.getGraph().getSelection().iterator(); itSelection.hasNext();) {
			Object selection =  itSelection.next();
			if (selection instanceof GraphNode) {
				result.add((GraphNode) selection);
			}
		}
		return result;
	}*/
}
