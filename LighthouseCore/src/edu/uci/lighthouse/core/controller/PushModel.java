package edu.uci.lighthouse.core.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.jpa.JPAUtilityException;
import edu.uci.lighthouse.model.jpa.LHEventDAO;

public class PushModel {
	
	private static Logger logger = Logger.getLogger(PushModel.class);

	private LighthouseModel model;
	
	public PushModel(LighthouseModel model) {
		this.model = model;
	}
	
	public void updateModelFromDelta(LighthouseDelta delta) throws JPAUtilityException {
	    HashMap<LighthouseClass,LighthouseEvent.TYPE> mapFireClassEvent = new HashMap<LighthouseClass,LighthouseEvent.TYPE>();
	    HashMap<LighthouseRelationship,LighthouseEvent.TYPE> mapFireRelEvent = new  HashMap<LighthouseRelationship,LighthouseEvent.TYPE>();
	   
	    LighthouseModelManager LhManager = new LighthouseModelManager(model);
	    // for each entity event
	    for (LighthouseEvent event : delta.getEvents()) {
			Object artifact = event.getArtifact();
			if (artifact instanceof LighthouseEntity) {
				logger.debug("updating: " + event.toString());
				LighthouseEntity deltaEntity = (LighthouseEntity) artifact;
				LhManager.addEvent(event);
				setClassesToFireUI(mapFireClassEvent, deltaEntity, event);
			}			
		}
	    // for each relationship event
	    for (LighthouseEvent event : delta.getEvents()) {
			Object artifact = event.getArtifact();
		    if (artifact instanceof LighthouseRelationship) {
		    	logger.debug("updating: " + event.toString());
		    	LighthouseRelationship deltaRelationship = (LighthouseRelationship) artifact;
		    	LhManager.addEvent(event);
		    	mapFireRelEvent.put(deltaRelationship, event.getType());
		    }
	    }
	    LhManager.saveEventsIntoDatabase(delta.getEvents());
	    
	    // Fire Modifications, call the UI
		fireClassModifications(mapFireClassEvent);
		fireRelationshipModifications(mapFireRelEvent);
	}

	public void updateCommittedEvents(List<String> listClazzFqn, String authorName) throws JPAUtilityException {
		new LHEventDAO().updateCommittedEvents(listClazzFqn,authorName);
	}
	
	/**
	 * @param mapClassEvent
	 * @param deltaEntity
	 * @param event
	 */
	private void setClassesToFireUI(
			HashMap<LighthouseClass, LighthouseEvent.TYPE> mapClassEvent,
			LighthouseEntity deltaEntity, LighthouseEvent event) {
		if (deltaEntity instanceof LighthouseClass) {
			LighthouseClass klass = (LighthouseClass) deltaEntity;
			mapClassEvent.put(klass, event.getType());
		} else {
			LighthouseClass klass = LighthouseModelManager.getMyClass(model, deltaEntity);
			if (klass != null) {
				if (!mapClassEvent.containsKey(klass)) { // I don't want to overwrite the ADD event
					mapClassEvent.put(klass, LighthouseEvent.TYPE.MODIFY);
				}
			}
		}
	}

	private void fireClassModifications(HashMap<LighthouseClass,LighthouseEvent.TYPE> map) {
		Set<LighthouseClass> classes = map.keySet();
		for (LighthouseClass c : classes) {
			model.fireClassChanged(c,map.get(c));
		}
	}

	private void fireRelationshipModifications(HashMap<LighthouseRelationship,LighthouseEvent.TYPE> map) {
		Set<LighthouseRelationship> relationships = map.keySet();
		for (LighthouseRelationship r : relationships) {
			model.fireRelationshipChanged(r,map.get(r));
		}
	}
	
}
