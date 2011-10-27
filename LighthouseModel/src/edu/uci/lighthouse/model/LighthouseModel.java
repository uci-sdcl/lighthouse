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
package edu.uci.lighthouse.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;


/**
 * Represents a Lighthouse Model, in which it has:
 * 
 * - List of Entities (from LighthouseAbstractModel)
 * - List of Relationships (from LighthouseAbstractModel)
 * - List of Events
 * 
 * */
public class LighthouseModel extends LighthouseAbstractModel {

	private static Logger logger = Logger.getLogger(LighthouseAbstractModel.class);
	
	private static LighthouseModel instance;

	/** Associate an Artifact(Entity or Relationship) with a list of events. */
	private HashMap<Object, LinkedHashSet<LighthouseEvent>> mapArtifactEvents = new HashMap<Object, LinkedHashSet<LighthouseEvent>>();
	
	/** List of all events*/
	private LinkedHashSet<LighthouseEvent> listEvents = new LinkedHashSet<LighthouseEvent>();
	
	protected LighthouseModel() {
	}
	
	public static LighthouseModel getInstance() {
		if (instance == null) {
			instance = new LighthouseModel();
		}
		return instance;
	}

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
			mapArtifactEvents.remove(artifact);
			listEvents.remove(event);
		} else {
			logger.warn("Artifact is null: " + event.toString());
		}
	}
	
	/**
	 * Get events related with a given Artifact
	 * 
	 * @param artifact
	 * 		{@link LighthouseEntity}
	 * 		OR
	 * 		{@link LighthouseRelationship} 	
	 * */
	public Collection<LighthouseEvent> getEvents(Object artifact){
		LinkedHashSet<LighthouseEvent> result = mapArtifactEvents.get(artifact);
		return result != null ? result : new LinkedHashSet<LighthouseEvent>();
	}
	
	public LinkedHashSet<LighthouseEvent> getListEvents() {
		return listEvents;
	}
	
	
	// Handle Listeners...

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
	
	public boolean isEmpty(){
		return (getListEvents().size()== 0
				&& getEntities().size() == 0
				&& getRelationships().size() == 0);
	}
	
}
