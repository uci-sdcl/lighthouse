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
import java.util.LinkedHashSet;

import edu.uci.lighthouse.model.LighthouseEvent.TYPE;

/**
 * It calculates the difference between two LighthouseFiles.
 * When a user save a file this class is responsible to generate a list of events
 * */
public class LighthouseDelta {

	private LinkedHashSet<LighthouseEvent> listEvents = new LinkedHashSet<LighthouseEvent>();
	private LighthouseAuthor author;

	public LighthouseDelta() {
	}

	public LighthouseDelta(LighthouseAuthor author, LighthouseFile oldModel,
			LighthouseFile newModel) {
		this.author = author;
		if (oldModel == null && newModel == null) {
			return;
		} else if (oldModel == null) {
			findAddedChanges(newModel);
		} else if (newModel == null) {
			findRemovedChanges(oldModel);
		} else {
			findEntityChanges(oldModel, newModel);
			findRelationshipChanges(oldModel, newModel);
		}
	}

	private void findEntityChanges(LighthouseFile oldModel,
			LighthouseFile newModel) {
		Collection<LighthouseEntity> listOldEntities = oldModel.getEntities();
		for (LighthouseEntity entity : listOldEntities) {
			if (!newModel.containsEntity(entity.getFullyQualifiedName())) {
				addEvent(new LighthouseEvent(TYPE.REMOVE, this.author, entity));
			}
		}
		Collection<LighthouseEntity> listNewEntities = newModel.getEntities();
		for (LighthouseEntity entity : listNewEntities) {
			if (!oldModel.containsEntity(entity.getFullyQualifiedName())) {
				addEvent(new LighthouseEvent(TYPE.ADD, this.author, entity));
			}
		}
	}

	private void findRelationshipChanges(LighthouseFile oldModel,
			LighthouseFile newModel) {
		Collection<LighthouseRelationship> listOldRel = oldModel
				.getRelationships();
		for (LighthouseRelationship oldRel : listOldRel) {
			if (!newModel.containsRelationship(oldRel)) {
				LighthouseRelationship rel = new LighthouseRelationship(oldRel
						.getFromEntity(), oldRel.getToEntity(), oldRel
						.getType());
				addEvent(new LighthouseEvent(TYPE.REMOVE, this.author, rel));
				addModifyEvent(rel);
			}
		}
		Collection<LighthouseRelationship> listNewRel = newModel
				.getRelationships();
		for (LighthouseRelationship newRel : listNewRel) {
			if (!oldModel.containsRelationship(newRel)) {
				LighthouseRelationship rel = new LighthouseRelationship(newRel
						.getFromEntity(), newRel.getToEntity(), newRel
						.getType());
				addEvent(new LighthouseEvent(TYPE.ADD, this.author, rel));
				addModifyEvent(rel);
			}
		}
	}

	/**
	 * Handle events in which the type is MODIFICATION
	 */
	private void addModifyEvent(LighthouseRelationship rel) {
		LighthouseEntity fromEntity = rel.getFromEntity();
		if (fromEntity instanceof LighthouseField
				|| fromEntity instanceof LighthouseMethod) {
			LighthouseEvent tempEventAdd = new LighthouseEvent(TYPE.ADD,
					this.author, fromEntity);
			LighthouseEvent tempEventRemove = new LighthouseEvent(TYPE.REMOVE,
					this.author, fromEntity);
			if (!(listEvents.contains(tempEventAdd) || listEvents
					.contains(tempEventRemove))) {
				addEvent(new LighthouseEvent(TYPE.MODIFY, this.author,
						fromEntity));
			}
		}
	}

	/**
	 * Handle events in which the type is ADD
	 */
	private void findAddedChanges(LighthouseFile model) {
		Collection<LighthouseEntity> listEntities = model.getEntities();
		for (LighthouseEntity entity : listEntities) {
			addEvent(new LighthouseEvent(TYPE.ADD, this.author, entity));
		}
		Collection<LighthouseRelationship> listRel = model.getRelationships();
		for (LighthouseRelationship rel : listRel) {
			addEvent(new LighthouseEvent(TYPE.ADD, this.author, rel));
		}
	}

	/**
	 * Handle events in which the type is REMOVE
	 */
	private void findRemovedChanges(LighthouseFile model) {
		Collection<LighthouseEntity> listEntities = model.getEntities();
		for (LighthouseEntity entity : listEntities) {
			addEvent(new LighthouseEvent(TYPE.REMOVE, this.author, entity));
		}
		Collection<LighthouseRelationship> listRel = model.getRelationships();
		for (LighthouseRelationship rel : listRel) {
			addEvent(new LighthouseEvent(TYPE.REMOVE, this.author, rel));
		}
	}

	/**
	 * This method exist only for test propose We use this because we need a way
	 * to populate a delta
	 * */
	public void addEvent(LighthouseEvent event) {
		listEvents.add(event);
	}

	public LinkedHashSet<LighthouseEvent> getEvents() {
		return listEvents;
	}

}
