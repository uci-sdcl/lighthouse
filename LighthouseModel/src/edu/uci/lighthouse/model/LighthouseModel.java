package edu.uci.lighthouse.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

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

	/** Associate an artifact(entity or relationship) with a list of events */
	private HashMap<Object, LinkedHashSet<LighthouseEvent>> mapArtifactEvents = new HashMap<Object, LinkedHashSet<LighthouseEvent>>();
	
	/** LIst of all events*/
	private LinkedHashSet<LighthouseEvent> listEvents = new LinkedHashSet<LighthouseEvent>();
	
	private CheckedOutEntities checkedOutEntities = new CheckedOutEntities();
	
	private HashMap<String, LighthouseAuthor> mapAuthor = new HashMap<String, LighthouseAuthor>();

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
			}
			listArtifactEvents.add(event);
			mapArtifactEvents.put(artifact,listArtifactEvents);
			event.setAuthor(putAuthor(event.getAuthor()));
			listEvents.add(event);
		} else {
			logger.warn("Artifact is null: " + event.toString());
		}
	}
	
	private LighthouseAuthor putAuthor(LighthouseAuthor newAuthor) {
		LighthouseAuthor authorFromModel = mapAuthor.get(newAuthor.getName());
		if (authorFromModel==null) {
			mapAuthor.put(newAuthor.getName(), newAuthor);
			return newAuthor;
		} else {
			return authorFromModel;
		}
	}
	
	public LinkedHashSet<LighthouseEvent> getListEvents() {
		return listEvents;
	}

	public LighthouseAuthor getAuthor(String name) {
		return mapAuthor.get(name);
	}
	
	public CheckedOutEntities getCheckedOutEntities() {
		return checkedOutEntities;
	}
	
	public void setCheckedOutEntity(String fqn, Date timestamp) {
		this.checkedOutEntities.put(fqn, timestamp);
	}
	
	/** List of listeners */
	private List<ILighthouseModelListener> listeners = new ArrayList<ILighthouseModelListener>();

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
