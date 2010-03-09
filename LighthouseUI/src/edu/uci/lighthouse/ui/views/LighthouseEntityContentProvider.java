package edu.uci.lighthouse.ui.views;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.Animation;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;

import edu.uci.lighthouse.model.ILighthouseUIModelListener;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.LighthouseEvent.TYPE;
import edu.uci.lighthouse.ui.utils.GraphUtils;

public class LighthouseEntityContentProvider implements IGraphEntityContentProvider, ILighthouseUIModelListener{

	private HashSet<String> cacheConnections = new HashSet<String>();

	private static Logger logger = Logger.getLogger(LighthouseEntityContentProvider.class);
	
	private GraphViewer viewer;
	
	@Override
	public Object[] getConnectedTo(Object entity) {
		logger.info("getConnectedTo");
		Collection<LighthouseClass> result = new LinkedList<LighthouseClass>();
		if (entity instanceof LighthouseClass) {
			LighthouseClass aClass = (LighthouseClass) entity;
			LighthouseModel model = LighthouseModel.getInstance();
			Collection<LighthouseClass> connections = model.getConnectTo(aClass);
			for (LighthouseClass iClass : connections) {
				if (!existsInCache(aClass,iClass)) {
					insertInCache(aClass,iClass);
					result.add(iClass);
				} else {
					logger.debug("duplicated "+aClass.getShortName()+"->"+iClass.getShortName());
				}
			}
		}
		return result.toArray();
	}
	
	private boolean existsInCache(LighthouseClass fromClass,
			LighthouseClass toClass) {
		String keyFrom = fromClass.getId() + toClass.getId();
		String keyTo = toClass.getId() + fromClass.getId();
		if (cacheConnections.contains(keyFrom)
				|| cacheConnections.contains(keyTo)) {
			return true;
		}
		return false;
	}
	
	private void insertInCache(LighthouseClass fromClass,
			LighthouseClass toClass) {
		String key = fromClass.getId() + toClass.getId();
		cacheConnections.add(key);
	}
	
	private void removeFromCache(LighthouseClass fromClass,
			LighthouseClass toClass) {
		String keyFrom = fromClass.getId() + toClass.getId();
		if (!cacheConnections.remove(keyFrom)) {
			String keyTo = toClass.getId() + fromClass.getId();
			cacheConnections.remove(keyTo);
		}
	}
	
	private void removeFromCache(GraphNode node) {
		Collection<GraphConnection> connections;
		connections = node.getSourceConnections();
		for (GraphConnection conn : connections) {
			removeFromCache((LighthouseClass)node.getData(),(LighthouseClass)conn.getDestination().getData());
		}
		connections = node.getTargetConnections();
		for (GraphConnection conn : connections) {
			removeFromCache((LighthouseClass)conn.getSource().getData(),(LighthouseClass)node.getData());
		}
	}
	
	private void removeRelationship(EntityConnectionData connection){
		viewer.removeRelationship(connection);
		viewer.removeRelationship(new EntityConnectionData(connection.dest,connection.source));
	}
	
	private void removeRelationships(GraphNode node) {
		Collection<GraphConnection> connections;
		connections = node.getSourceConnections();
		for (GraphConnection conn : connections) {
			removeRelationship((EntityConnectionData)conn.getData());
		}
		connections = node.getTargetConnections();
		for (GraphConnection conn : connections) {
			removeRelationship((EntityConnectionData)conn.getData());
		}
	}

	@Override
	public Object[] getElements(Object inputElement) {
		Collection<LighthouseClass> result = new LinkedList<LighthouseClass>();
		if (inputElement instanceof LighthouseModel) {
			cacheConnections.clear();
			LighthouseModel model = (LighthouseModel) inputElement;
			logger.info("getElements: "+model.getAllClasses().size());
			result = model.getAllClasses();
		}
		return result.toArray();
	}

	@Override
	public void dispose() {
		logger.info("dispose");
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {	
		logger.info("inputChanged");
		
		// Ensure that all the arguments are legal
		Assert.isLegal(viewer instanceof GraphViewer, "Invalid viewer, only GraphViewer is supported.");
		this.viewer = (GraphViewer) viewer;
		
		if (oldInput instanceof LighthouseModel){
			LighthouseModel oldModel = (LighthouseModel) oldInput;
			oldModel.removeModelListener(this);
		}
		
		if (newInput instanceof LighthouseModel){
			LighthouseModel newModel = (LighthouseModel) newInput;
			newModel.addModelListener(this);
		}
	}

	@Override
	public void classChanged(LighthouseClass aClass, TYPE type) {
		logger.info("classChanged: " + aClass.getShortName()+" ("+type+")");
		GraphItem item = viewer.findGraphItem(aClass);
		if (item == null) {
			switch (type) {
			case ADD:
				if (!filterElement(viewer.getInput(),aClass)) {
					viewer.addNode(aClass);
					logger.debug("Class "+aClass.getShortName()+" added.");
					viewer.getGraphControl().applyLayout();					
				}
				break;
			default:
				logger.debug("Refreshing viewer ("+aClass.getShortName()+":"+type.toString()+")");
				// Rebuild the view, but keeps the old positions of elements.
				viewer.refresh();
				break;
			}
		} else {
			switch (type) {
			case ADD: case MODIFY:
				Animation.markBegin();
				GraphUtils.rebuildFigure((GraphNode) item);
				Animation.run(150);
				break;
			case REMOVE:
				LighthouseModel model = LighthouseModel.getInstance();
				if (!model.containsEntity(aClass.getFullyQualifiedName())) {
					removeFromCache((GraphNode)item);
					removeRelationships((GraphNode)item);
					viewer.removeNode(aClass);
				} else {
					Animation.markBegin();
					GraphUtils.rebuildFigure((GraphNode) item);
					Animation.run(150);
				}
				break;
			}
		}
	}

	@Override
	public void modelChanged() {
		logger.info("modelChanged()");
		//Rebuild the view, with the same input
		viewer.refresh();
		viewer.getGraphControl().applyLayout();
	}

	@Override
	public void relationshipChanged(LighthouseRelationship relationship,
			TYPE type) {
		logger.info("relationshipChanged: "+relationship+" ("+type+")");
		EntityConnectionData connection = getEntityConnectionData(relationship);
		if (connection != null){
			if (existsInCache((LighthouseClass)connection.source, (LighthouseClass)connection.dest)) {
				switch (type) {
				case REMOVE:
					removeFromCache((LighthouseClass)connection.source, (LighthouseClass)connection.dest);
					removeRelationship(connection);
					if (filterElement(viewer.getInput(),connection.source)) {
						viewer.removeNode(connection.source);
					}
					if (filterElement(viewer.getInput(),connection.dest)) {
						viewer.removeNode(connection.dest);
					}
					break;
				}
			} else {
				switch (type) {
				case ADD:
					if (!filterElement(viewer.getInput(),connection.source)&&!filterElement(viewer.getInput(),connection.dest)&&!filterElement(viewer.getInput(),connection)) {
						viewer.addNode(connection.source);
						viewer.addNode(connection.dest);
						viewer.getGraphControl().applyLayout();	
						viewer.addRelationship(connection);
						insertInCache((LighthouseClass)connection.source, (LighthouseClass)connection.dest);
					}
					break;
				}
			}
		}
//		GraphItem item = viewer.findGraphItem(connection);
//		if (item = null) {
//			switch (type) {
//			case ADD:
//				viewer.addRelationship(connection);
//				break;
//			}
//		} else {
//			switch (type) {
//			case REMOVE:
//				viewer.removeRelationship(connection);
//				break;
//			}
//		}
	}
	
	private EntityConnectionData getEntityConnectionData(LighthouseRelationship r){
		LighthouseModelManager manager = new LighthouseModelManager(
				LighthouseModel.getInstance());
		LighthouseClass fromClass = manager.getMyClass(r.getFromEntity());
		LighthouseClass toClass = manager.getMyClass(r.getToEntity());
		if (fromClass != null && toClass != null && !fromClass.equals(toClass)){
			return new EntityConnectionData(fromClass,toClass);
		}
		return null;
	}
	
	private boolean filterElement(Object parent, Object element) {
		ViewerFilter[] filters = viewer.getFilters();
		for (int i = 0; i < filters.length; i++) {
			boolean selected = filters[i].select(viewer, parent, element);
			if (!selected) {
				return true;
			}
		}
		return false;
	}
}
