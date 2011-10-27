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

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import edu.uci.lighthouse.model.LighthouseRelationship.TYPE;

/**
 * This class is responsible for building lighthouse model objects. Its purpose is to
 * assist in constructing models from source code.
 * 
 */
public class LighthouseModelManager {
	
	private static Logger logger = Logger.getLogger(LighthouseModelManager.class);
	
	protected LighthouseModel model;

	public LighthouseModelManager(LighthouseModel model) {
		super();
		this.model = model;
	}

	private LighthouseEntity addEntity(LighthouseEntity newEntity){
		LighthouseEntity entity = model.getEntity(newEntity.getFullyQualifiedName());
		if (entity!=null) {
			return entity;
		} else {
			model.addEntity(newEntity);
			return newEntity;	
		}
	}

	private LighthouseRelationship addRelationship(LighthouseRelationship newRelationship){
		LighthouseRelationship relationship = model.getRelationship(newRelationship);
		if (relationship == null) {
			relationship = newRelationship;
		}
		handleEntitiesNotInsideClass(relationship);		
		model.addRelationship(relationship);
		return newRelationship;
	}
	
	/** Need this method for add the Entities: ExternalClass and Modifiers in the LHBaseFile
	 * because they have not TYPE.INSIDE relationship */
	private void handleEntitiesNotInsideClass(
			LighthouseRelationship relationship) {
		LighthouseEntity fromEntity = relationship.getFromEntity();
		LighthouseEntity toEntity = relationship.getToEntity();
		if (fromEntity instanceof LighthouseExternalClass) {
			addEntity(fromEntity);
		}
		if (toEntity instanceof LighthouseExternalClass) {
			addEntity(toEntity);
		}
		if (relationship.getType()==TYPE.MODIFIED_BY) {
			addEntity(toEntity);
		}
	}
	
	/** Add <code>event</code> in the LighthouseModel, however do not add the event in the database*/
	public void addEvent(LighthouseEvent event) {
		Object artifact = addArtifact(event.getArtifact());
		event.setArtifact(artifact);
		model.addEvent(event);
	}
	
	/**
	 * @param artifact
	 * 		{@link LighthouseEntity}
	 * 		OR
	 * 		{@link LighthouseRelationship}
	 * */
	public Object addArtifact(Object artifact) {
		if (artifact instanceof LighthouseEntity) {
			LighthouseEntity entity = addEntity((LighthouseEntity) artifact);
			return entity;
		} else if (artifact instanceof LighthouseRelationship) {
			LighthouseRelationship relationship = addRelationship((LighthouseRelationship) artifact);
			return relationship;
		}
		return null;
	}
	
	public LighthouseEntity getEntity(String fqn) {
		return model.getEntity(fqn);
	}
	
	/** 
	 * Used to import a new project in the database
	 * 
	 * After parsing a new Project we end up having a list of entities and a list of relationship.
	 * Then we need to populate the LighthouseModel with events based in those lists.
	 * This method is responsible to create such events, and add then in the model.
	 * 
	 * */
	public Collection<LighthouseEvent> createEventsAndSaveInLhModel(LighthouseAuthor author, Collection<LighthouseEntity> listEntities, Collection<LighthouseRelationship> listLighthouseRelationships) {
		Collection<LighthouseEvent> listEvents = new LinkedList<LighthouseEvent>();
		for (LighthouseEntity entity : listEntities) {
			LighthouseEvent event = new LighthouseEvent(LighthouseEvent.TYPE.ADD,author,entity);
			event.setCommitted(true);
			event.setCommittedTime(new Date(0));
			event.setTimestamp(new Date(0));
			addArtifact(entity);
			listEvents.add(event);
			logger.debug("Add entity in model: " + entity);
		}
		for (LighthouseRelationship rel : listLighthouseRelationships) {
			LighthouseEvent event = new LighthouseEvent(LighthouseEvent.TYPE.ADD,author,rel);
			event.setCommitted(true);
			event.setCommittedTime(new Date(0));
			event.setTimestamp(new Date(0));
			addArtifact(rel);
			listEvents.add(event);
			logger.debug("Add relationship in model: " + rel);
		}
		return listEvents;
	}

	public void removeRelationship(LighthouseRelationship rel) {
		model.removeRelationship(rel);
	}
	
	/**
	 * Remove Artifacts that are inside of the list of classes,
	 * and also remove the Events related to those Artifacts.
	 * Used by UpdateAction.
	 * */
	public void removeArtifactsAndEvents(Collection<String> listClazzFqn) {
		Collection<LighthouseEntity> listEntity = LighthouseModelUtil.getEntitiesInsideClasses(model, listClazzFqn);
		Collection<LighthouseRelationship> listRel = LighthouseModelUtil.getRelationships(model, listEntity);
		LinkedHashSet<LighthouseEvent> listEvents = LighthouseModelUtil.getEventsByListEntityAndRel(model, listEntity, listRel);
		for (LighthouseEvent event : listEvents) {
			model.removeEvent(event);
		}
		for (LighthouseRelationship rel : listRel) {
			model.removeRelationship(rel);
		}
		for (LighthouseEntity entity : listEntity) {
			model.removeEntity(entity);
		}
	}

	/**
	 * After perform a commit we need to clean the committed events
	 * @param listClazzFqn
	 */
	public void removeCommittedEventsAndArtifacts(Collection<String> listClazzFqn) {
		Collection<LighthouseEntity> listEntity = LighthouseModelUtil.getEntitiesInsideClasses(model, listClazzFqn);
		Collection<LighthouseRelationship> listRel = LighthouseModelUtil.getRelationships(model, listEntity);
		LinkedHashSet<LighthouseEvent> listEvents = LighthouseModelUtil.getEventsByListEntityAndRel(model, listEntity, listRel);
		for (LighthouseEvent event : listEvents) {
			if (event.isCommitted()) {
				// If I am allowed to commit, it is supposed that I am working on the last version,
				// so I can remove every committed event, because all of them will be in the past
				if (event.getType()==LighthouseEvent.TYPE.REMOVE) {
					model.removeEventAndArtifact(event);
				} else {
					model.removeEvent(event);
				}
			}
		}
	}

	/**
	 * Returns the associate class or <code>null</code> otherwise.
	 * 
	 * @param model
	 * @param e
	 * @return
	 */
	public LighthouseEntity getMyClass(LighthouseEntity e){
		String classFqn = null;
		if (e instanceof LighthouseClass || e instanceof LighthouseInterface){
			classFqn = e.getFullyQualifiedName();
		} else if (e instanceof LighthouseField){
			classFqn = e.getFullyQualifiedName().replaceAll("(\\.\\w+)\\z", "");
		} else if (e instanceof LighthouseMethod){
			classFqn = e.getFullyQualifiedName().replaceAll("\\.[\\<\\w]*\\w+[\\>\\w]*\\(.*\\)","");
		}
		LighthouseEntity c = model.getEntity(classFqn);
//		if (c instanceof LighthouseClass){
//			return (LighthouseClass)c;
//		}
		return c;
	}

	/**
	 * Copies all content between 'from' model to 'to' model. 
	 * 
	 * @param from
	 * @param to
	 */
	public void modelCopy(LighthouseModel from){
		model.assignTo(from);
	}

	
}
