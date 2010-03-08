package edu.uci.lighthouse.ui.views;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.Animation;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.GraphNode;

import edu.uci.lighthouse.ui.graph.IUmlClass;
import edu.uci.lighthouse.ui.utils.GraphUtils;
import edu.uci.lighthouse.views.filters.IClassFilter;

public class FilterManager {

	private StructuredViewer viewer;
	private Collection<IClassFilter> classFilters = new LinkedList<IClassFilter>();
	private static FilterManager instance;
	
	private static Logger logger = Logger.getLogger(FilterManager.class);
	
	FilterManager(StructuredViewer viewer) {
		this.viewer = viewer;
		instance = this;
	}

	public void addClassFilter(IClassFilter filter){
		if (!classFilters.contains(filter)) {
			classFilters.add(filter);
			
			refreshAllFigures();
			//FIXME: viewer.refresh is making the connections disappear.
//			viewer.refresh();
		}
	}
	
	public void removeClassFilter(IClassFilter filter){
		if (classFilters.remove(filter)){
			refreshAllFigures();
		}
//		viewer.refresh();
	}

	public void addViewerFilter(ViewerFilter filter){
		boolean contains = false;
		ViewerFilter[] filters = viewer.getFilters();
		for (ViewerFilter viewerFilter : filters) {
			if (viewerFilter.equals(filter)) {
				contains = true;
				break;
			}
		}
		if (!contains) {
			viewer.addFilter(filter);
		} 
	}
	
	public void removeViewerFilter(ViewerFilter filter){
		viewer.removeFilter(filter);
	}
	
	public IClassFilter[] getClassFilters(){
		return classFilters.toArray(new IClassFilter[0]);
	}
	
	public ViewerFilter[] getViewerFilters(){
		return viewer.getFilters();
	}
	
	public static FilterManager getInstance(){
		if (instance == null){
			RuntimeException e = new RuntimeException("FilterManager instance not created.");
			logger.error(e,e);
			throw e;
		}
		return instance;
	}
	
	private void refreshAllFigures() {
		if (viewer instanceof GraphViewer) {
			GraphViewer gv = (GraphViewer) viewer;
			Animation.markBegin();
			for (Iterator itNodes = gv.getGraphControl().getNodes().iterator(); itNodes
					.hasNext();) {
				GraphNode node = (GraphNode) itNodes.next();
				GraphUtils.rebuildFigure(node);
			}
			Animation.run(150);
		}
	}
	
}
