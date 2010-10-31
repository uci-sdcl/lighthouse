package edu.uci.lighthouse.model;

import java.util.Collection;
import java.util.LinkedHashSet;

import edu.uci.lighthouse.model.LighthouseRelationship.TYPE;

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
			//FIXME: not verifying if it is a null pointer
			listEntity.add(clazz);
			listEntity.addAll(getEntitiesInsideClass(model, clazz));
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
	
}
