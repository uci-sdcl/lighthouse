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
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.uci.lighthouse.model.LighthouseRelationship.TYPE;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.LHRelationshipDAO;

public class LighthouseModelUtil {

	/**
	 * Verify if the <code>relationship</code> "BELONGS" to a class(listEntitiesInside)
	 * */
	public static boolean isValidRelationship(LighthouseRelationship relationship, Collection<LighthouseEntity> listEntitiesInside) {
		if (!listEntitiesInside.contains(relationship.getFromEntity())) {
			if (	relationship.getType()==LighthouseRelationship.TYPE.CALL 
					|| relationship.getType()==LighthouseRelationship.TYPE.USES
					|| relationship.getType()==LighthouseRelationship.TYPE.HOLDS
					|| relationship.getType()==LighthouseRelationship.TYPE.RECEIVES
					|| relationship.getType()==LighthouseRelationship.TYPE.RETURN
					|| relationship.getType()==LighthouseRelationship.TYPE.ACCESS) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Get Entities INSIDE a class
	 * */
	private static LinkedHashSet<LighthouseEntity> getEntitiesInsideClass(LighthouseModel model, LighthouseEntity clazz) {
		LinkedHashSet<LighthouseEntity> listEntity = new LinkedHashSet<LighthouseEntity>();
		Collection<LighthouseRelationship> listRelInside = model.getRelationshipsTo(clazz,TYPE.INSIDE);
		for (LighthouseRelationship rel : listRelInside) {
			LighthouseEntity entity = rel.getFromEntity();
			listEntity.add(entity);
			if (entity instanceof LighthouseClass) {
				listEntity.addAll(getEntitiesInsideClass(model,entity));
			}
		}
		return listEntity;
	}
	
	/**
	 * for each class - Get Entities INSIDE a class
	 * */
	public static Collection<LighthouseEntity> getEntitiesInsideClasses(LighthouseModel model, Collection<String> listClazzFqn) {
		LinkedHashSet<LighthouseEntity> listEntity = new LinkedHashSet<LighthouseEntity>();
		for (String clazzFqn : listClazzFqn) {
			LighthouseEntity clazz = model.getEntity(clazzFqn);
			if (clazz!=null) {
				listEntity.add(clazz);
				listEntity.addAll(getEntitiesInsideClass(model, clazz));
			}
		}
		return listEntity;
	}
	
	/**
	 * for each class - Get events related to entities/relationship that are INSIDE a class
	 * */
	public static Collection<LighthouseEvent> getEventsInside(LighthouseModel model, Collection<String> listClazzFqn) {
		Collection<LighthouseEntity> listEntity = getEntitiesInsideClasses(model, listClazzFqn);
		Collection<LighthouseRelationship> listRel = getRelationships(model, listEntity);
		LinkedHashSet<LighthouseEvent> listEvents = getEventsByListEntityAndRel(model, listEntity, listRel);
		return listEvents;
	}
	
	/**
	 * Get Events related to a list of Entities and Relationship
	 * */
	public static LinkedHashSet<LighthouseEvent> getEventsByListEntityAndRel(LighthouseModel model, Collection<LighthouseEntity> listEntity, Collection<LighthouseRelationship> listRel) {
		LinkedHashSet<LighthouseEvent> listEvents = new LinkedHashSet<LighthouseEvent>();
		for (LighthouseEntity entity : listEntity) {
			listEvents.addAll(model.getEvents(entity));
		}
		for (LighthouseRelationship rel : listRel) {
			listEvents.addAll(model.getEvents(rel));
		}
		return listEvents;
	}

	/**
	 * Get Relationship related to a list of Entities
	 * */
	public static Collection<LighthouseRelationship> getRelationships(LighthouseModel model, Collection<LighthouseEntity> listEntity) {
		LinkedHashSet<LighthouseRelationship> listRel = new LinkedHashSet<LighthouseRelationship>();
		for (LighthouseEntity entity : listEntity) {
			for (LighthouseRelationship rel : model.getRelationshipsFrom(entity)) {
				listRel.add(rel);
			}
			for (LighthouseRelationship rel : model.getRelationshipsTo(entity)) {
				if (LighthouseModelUtil.isValidRelationship(rel, listEntity)) {
					listRel.add(rel);
				}
			}
		}
		return listRel;
	}
	
	/**
	 * @param fqnClazz
	 * @return map with class/InnerClass -> list of entities inside a class/InnerClass
	 * @throws JPAException
	 */
	public static HashMap<LighthouseClass, Collection<LighthouseEntity>> selectEntitiesInsideClass(String fqnClazz) throws JPAException {
		HashMap<LighthouseClass, Collection<LighthouseEntity>> map = new HashMap<LighthouseClass, Collection<LighthouseEntity>>();
		selectEntitiesInsideClass(map, fqnClazz);
		return map;
	}
	
	/**
	 * Going to the database to return the entities inside a class
	 * Recursive method
	 * @throws JPAException 
	 * */
	private static void selectEntitiesInsideClass(HashMap<LighthouseClass, Collection<LighthouseEntity>> map, String fqnClazz) throws JPAException {
		LighthouseClass clazz = new LighthouseClass(fqnClazz);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("relType", LighthouseRelationship.TYPE.INSIDE);
		parameters.put("toEntity", clazz);
		List<LighthouseEntity> subListEntitiesInside = new LHRelationshipDAO().executeNamedQueryGetFromEntityFqn("LighthouseRelationship.findFromEntityByTypeAndToEntity", parameters);
		
		Collection<LighthouseEntity> listEntities = map.get(clazz);
		if (listEntities == null) {
			listEntities = new LinkedList<LighthouseEntity>();
			map.put(clazz, listEntities);
		}
		
		for (LighthouseEntity entity : subListEntitiesInside) {
			if (entity instanceof LighthouseClass || entity instanceof LighthouseInterface) { // That is a Inner class
				selectEntitiesInsideClass(map, entity.getFullyQualifiedName());
			}
		}
		
		listEntities.addAll(subListEntitiesInside);
	}
	
}
