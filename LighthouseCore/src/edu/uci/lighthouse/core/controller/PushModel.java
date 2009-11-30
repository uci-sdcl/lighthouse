package edu.uci.lighthouse.core.controller;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.log4j.Logger;

import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseModelUtil;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.LighthouseRelationship.TYPE;
import edu.uci.lighthouse.model.jpa.JPAUtilityException;
import edu.uci.lighthouse.model.jpa.LHEventDAO;

public class PushModel {
	
	private static Logger logger = Logger.getLogger(PushModel.class);

	private LighthouseModel model;
	
	public PushModel(LighthouseModel model) {
		this.model = model;
	}
	
	public void updateModelFromDelta(LighthouseDelta delta) throws JPAUtilityException {   
	    LighthouseModelManager LhManager = new LighthouseModelManager(model);
	    // for each entity event
	    for (LighthouseEvent event : delta.getEvents()) {
			Object artifact = event.getArtifact();
			if (artifact instanceof LighthouseEntity) {
				logger.debug("updating: " + event.toString());
				LhManager.addEvent(event);
			}			
		}
	    // for each relationship event
	    for (LighthouseEvent event : delta.getEvents()) {
			Object artifact = event.getArtifact();
		    if (artifact instanceof LighthouseRelationship) {
		    	logger.debug("updating: " + event.toString());
		    	LhManager.addEvent(event);
		    }
	    }
	    LhManager.saveEventsIntoDatabase(delta.getEvents());
	}

	public Collection<LighthouseEvent> updateCommittedEvents(List<String> listClazzFqn, Date svnCommittedTime, LighthouseAuthor author) throws JPAUtilityException {
		LinkedHashSet<LighthouseEntity> listEntity = new LinkedHashSet<LighthouseEntity>();
		for (String clazzFqn : listClazzFqn) {
			LighthouseEntity clazz = model.getEntity(clazzFqn);
			listEntity.add(clazz);
			listEntity.addAll(getEntitiesInsideClass(clazz));
		}
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
		LinkedHashSet<LighthouseEvent> listEvents = new LinkedHashSet<LighthouseEvent>();
		for (LighthouseEntity entity : listEntity) {
			listEvents.addAll(model.getEvents(entity));
		}
		for (LighthouseRelationship rel : listRel) {
			listEvents.addAll(model.getEvents(rel));
		}
		
		LinkedHashSet<LighthouseEvent> listEventsToCommitt = new LinkedHashSet<LighthouseEvent>();
		for (LighthouseEvent event : listEvents) {
			if (event.getAuthor().equals(author)) {
				if (!event.isCommitted()) {
					event.setCommitted(true);
					event.setCommittedTime(svnCommittedTime);
					listEventsToCommitt.add(event);
				}
			}
		}
		new LHEventDAO().updateCommittedEvents(listEventsToCommitt, svnCommittedTime);
		return listEventsToCommitt;
	}
	
	private LinkedHashSet<LighthouseEntity> getEntitiesInsideClass(LighthouseEntity clazz) {
		LinkedHashSet<LighthouseEntity> listEntity = new LinkedHashSet<LighthouseEntity>();
		Collection<LighthouseRelationship> listRelInside = model.getRelationshipsTo(clazz,TYPE.INSIDE);
		for (LighthouseRelationship rel : listRelInside) {
			LighthouseEntity entity = rel.getFromEntity();
			listEntity.add(entity);
		}
		return listEntity;
	}
	
	
}
