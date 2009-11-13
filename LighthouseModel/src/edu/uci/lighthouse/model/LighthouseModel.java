package edu.uci.lighthouse.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;

/**
 * Represents the Lighthouse Model.
 * 
 * A model contains a list of entities. Entities can be associated through
 * relationships.
 * 
 * @author tproenca
 * 
 */
public class LighthouseModel extends LighthouseAbstractModel {

	private static Logger logger = Logger.getLogger(LighthouseAbstractModel.class);
	
	/** Model instance. */
	private static LighthouseModel instance;

	/** Associate an artifact(entity or relationship) with a list of events. */
	private HashMap<Object, LinkedHashSet<LighthouseEvent>> mapArtifactEvents = new HashMap<Object, LinkedHashSet<LighthouseEvent>>();
	
	/** LIst of all events*/
	private Collection<LighthouseEvent> listEvents = new LinkedHashSet<LighthouseEvent>();
	
	/** List of listeners */
	private Collection<ILighthouseModelListener> listeners = new ArrayList<ILighthouseModelListener>();
	
	protected LighthouseModel() {
	}
	
	/**
	 * Returns the Lighthouse Model instance.
	 * 
	 * @return the model instance
	 */
	public static LighthouseModel getInstance() {
		if (instance == null) {
			instance = new LighthouseModel();
		}
		return instance;
	}
	
	// only ModelManager is allowed to call this method
	final synchronized void addEvent(LighthouseEvent event) {
		Object artifact = event.getArtifact();
		if (artifact!=null) {
			LinkedHashSet<LighthouseEvent> listArtifactEvents = mapArtifactEvents.get(artifact);
			if (listArtifactEvents==null) {
				listArtifactEvents = new LinkedHashSet<LighthouseEvent>();
				mapArtifactEvents.put(artifact,listArtifactEvents);
			}
			if (listArtifactEvents.contains(event)){
				for(LighthouseEvent evt: listArtifactEvents){
					if (evt.equals(event)){
						evt.setCommitted(event.isCommitted());
						evt.setCommittedTime(event.getCommittedTime());
					}
				}
			} else {
				listArtifactEvents.add(event);
				listEvents.add(event);
			}
		} else {
			logger.warn("Artifact is null: " + event.toString());
		}
	}
	
	final synchronized void removeEvent(LighthouseEvent event) {
		Object artifact = event.getArtifact();
		if (artifact!=null) {
			LinkedHashSet<LighthouseEvent> listArtifactEvents = mapArtifactEvents.get(artifact);
			if (listArtifactEvents!=null) {
				listArtifactEvents.remove(event);
			}
		} else {
			logger.warn("Artifact is null: " + event.toString());
		}
	}
	
	public Collection<LighthouseEvent> getEvents(Object artifact){
		Collection<LighthouseEvent> result = mapArtifactEvents.get(artifact);
		return result != null ? result : new ArrayList<LighthouseEvent>();
	}
	
//	public Collection<LighthouseEvent> getListEvents() {
//		Collection<LighthouseEvent> result = new LinkedList<LighthouseEvent>();
//		Collection<LighthouseEvent> entities = new TreeSet<LighthouseEvent>();
//		Collection<LighthouseEvent> relationships = new TreeSet<LighthouseEvent>();
//		for(Entry<Object, TreeSet<LighthouseEvent>> entry : mapArtifactEvents.entrySet()){
//			if (entry.getKey() instanceof LighthouseEntity) {
//				entities.addAll(entry.getValue());
//			} else if (entry.getKey() instanceof LighthouseRelationship){
//				relationships.addAll(entry.getValue());
//			}
////			System.out.println("size: "+relationships.size()+" "+entry.getValue());
//		}
//		result.addAll(entities);
//		result.addAll(relationships);
//		return result;
//	}
	
	public Collection<LighthouseEvent> getListEvents() {
		return listEvents;
	}

	public void addModelListener(ILighthouseModelListener listener) {
		listeners.add(listener);
	}

	public void removeModelListener(ILighthouseModelListener listener) {
		listeners.remove(listener);
	}

	public void fireModelChanged() {
		for (final ILighthouseModelListener l : listeners) {
			if (l instanceof ILighthouseUIModelListener) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						l.modelChanged();
					}
				});
			} else {
				l.modelChanged();
			}
		}
	}

	public void fireClassChanged(final LighthouseClass c,
			final LighthouseEvent.TYPE type) {
		for (final ILighthouseModelListener l : listeners) {
			if (l instanceof ILighthouseUIModelListener) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						l.classChanged(c, type);
					}
				});
			} else {
				l.classChanged(c, type);
			}
		}
	}

	public void fireRelationshipChanged(final LighthouseRelationship r,
			final LighthouseEvent.TYPE type) {
		for (final ILighthouseModelListener l : listeners) {
			if (l instanceof ILighthouseUIModelListener) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						l.relationshipChanged(r, type);
					}
				});
			} else {
				l.relationshipChanged(r, type);
			}
		}
	}
	
}
