package edu.uci.lighthouse.model;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import edu.uci.lighthouse.model.LighthouseRelationship.TYPE;

public class LighthouseModelUtil {

	// used by BuildLHBaseFile and PullModel
	public static boolean isValidRelationship(Object artifact, Collection<LighthouseEntity> listEntitiesInside) {
		boolean result = true; 
		LighthouseRelationship rel = (LighthouseRelationship) artifact;
		if (!listEntitiesInside.contains(rel.getFromEntity())) {
			if (	rel.getType()==LighthouseRelationship.TYPE.CALL 
					|| rel.getType()==LighthouseRelationship.TYPE.USES
					|| rel.getType()==LighthouseRelationship.TYPE.HOLDS
					|| rel.getType()==LighthouseRelationship.TYPE.RECEIVES
					|| rel.getType()==LighthouseRelationship.TYPE.RETURN) {
				result = false;
			}
		}
		return result;
	}
	
	// used by checkout
	public static boolean wasCommittedEventRemoved(List<LighthouseEvent> listEvents, LighthouseEvent paramEvent) {
		for (LighthouseEvent event : listEvents) {
			if (event.getArtifact().equals(paramEvent.getArtifact())) {
				if (	event.isCommitted() &&
						event.getType() == LighthouseEvent.TYPE.REMOVE && 
						(	event.getCommittedTime().after(paramEvent.getCommittedTime()) ||
							event.getCommittedTime().equals(paramEvent.getCommittedTime()) ) ) {
					return true;
				}
			}
		}
		return false;
	}
	
	// used by BuildBaseFile
	public static boolean wasEventRemoved(List<LighthouseEvent> listEvents, LighthouseEvent paramEvent, LighthouseAuthor paramAuthor) {
		for (LighthouseEvent event : listEvents) {
			if (event.getArtifact().equals(paramEvent.getArtifact())) {
				if (event.isCommitted()) {
					if (event.getType() == LighthouseEvent.TYPE.REMOVE
							&& (event.getCommittedTime().after(paramEvent.getCommittedTime()) 
							|| event.getCommittedTime().equals(paramEvent.getCommittedTime()))) {
						return true;
					}
				} else { // if is not committed - take the author in consideration
					if ( event.getAuthor().equals(paramAuthor)
							&& event.getType() == LighthouseEvent.TYPE.REMOVE
							&& (event.getTimestamp().after(paramEvent.getTimestamp()) 
							|| event.getTimestamp().equals(paramEvent.getTimestamp()))) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static Collection<LighthouseEvent> getEventsInside(LighthouseModel model, Collection<String> listClazzFqn) {
		Collection<LighthouseEntity> listEntity = getEntitiesInsideClasses(model, listClazzFqn);
		Collection<LighthouseRelationship> listRel = getRelationships(model, listEntity);
		LinkedHashSet<LighthouseEvent> listEvents = getEventsInside(model, listEntity, listRel);
		return listEvents;
	}

	public static LinkedHashSet<LighthouseEvent> getEventsInside(
			LighthouseModel model, Collection<LighthouseEntity> listEntity,
			Collection<LighthouseRelationship> listRel) {
		LinkedHashSet<LighthouseEvent> listEvents = new LinkedHashSet<LighthouseEvent>();
		for (LighthouseEntity entity : listEntity) {
			listEvents.addAll(model.getEvents(entity));
		}
		for (LighthouseRelationship rel : listRel) {
			listEvents.addAll(model.getEvents(rel));
		}
		return listEvents;
	}

	public static Collection<LighthouseRelationship> getRelationships(
			LighthouseModel model, Collection<LighthouseEntity> listEntity) {
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

	public static Collection<LighthouseEntity> getEntitiesInsideClasses(
			LighthouseModel model, Collection<String> listClazzFqn) {
		LinkedHashSet<LighthouseEntity> listEntity = new LinkedHashSet<LighthouseEntity>();
		for (String clazzFqn : listClazzFqn) {
			LighthouseEntity clazz = model.getEntity(clazzFqn);
			listEntity.add(clazz);
			listEntity.addAll(getEntitiesInsideClass(model, clazz));
		}
		return listEntity;
	}
			
	private static LinkedHashSet<LighthouseEntity> getEntitiesInsideClass(LighthouseModel model, LighthouseEntity clazz) {
		LinkedHashSet<LighthouseEntity> listEntity = new LinkedHashSet<LighthouseEntity>();
		Collection<LighthouseRelationship> listRelInside = model.getRelationshipsTo(clazz,TYPE.INSIDE);
		for (LighthouseRelationship rel : listRelInside) {
			LighthouseEntity entity = rel.getFromEntity();
			listEntity.add(entity);
		}
		return listEntity;
	}
	
}
