package edu.uci.lighthouse.core.controler;

import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseModelManagerPersistence;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.jpa.JPAUtilityException;

public class PushModel {
	
	private static Logger logger = Logger.getLogger(PushModel.class);

	private LighthouseModel model;
	
	public PushModel(LighthouseModel model) {
		this.model = model;
	}
	
	public void execute(LighthouseDelta delta) throws JPAUtilityException {
	    HashMap<LighthouseClass,LighthouseEvent.TYPE> mapFireClassEvent = new HashMap<LighthouseClass,LighthouseEvent.TYPE>();
	    HashMap<LighthouseRelationship,LighthouseEvent.TYPE> mapFireRelEvent = new  HashMap<LighthouseRelationship,LighthouseEvent.TYPE>();

	    // for each entity event
	    for (LighthouseEvent event : delta.getEvents()) {
	    	logger.debug("updating: " + event.toString());
			Object artifact = event.getArtifact();
			if (artifact instanceof LighthouseEntity) {
				LighthouseEntity deltaEntity = (LighthouseEntity) artifact;
				LighthouseEntity lighthouseEntity = model.getEntity(deltaEntity.getFullyQualifiedName());
				switch (event.getType()) {
				case ADD:
					new LighthouseModelManagerPersistence(model).addEvent(event);
					setClassesToFireUI(mapFireClassEvent, deltaEntity, event);
					break;
				case MODIFY:
					event.setArtifact(lighthouseEntity);
					new LighthouseModelManagerPersistence(model).addEvent(event);
					setClassesToFireUI(mapFireClassEvent, lighthouseEntity, event);
					break;
				case REMOVE:
					event.setArtifact(lighthouseEntity);
					new LighthouseModelManagerPersistence(model).addEvent(event);				
					setClassesToFireUI(mapFireClassEvent, lighthouseEntity, event);
					break;
				}
				setClassesToFireUI(mapFireClassEvent, deltaEntity, event); // FIXME Why do we need this line?
			}			
		} // end-for-delta

	    // for each relationship event
	    for (LighthouseEvent event : delta.getEvents()) {
	    	logger.debug("updating: " + event.toString());
			Object artifact = event.getArtifact();
		    if (artifact instanceof LighthouseRelationship) {
		    	LighthouseRelationship deltaRelationship = (LighthouseRelationship) artifact;
		    	LighthouseRelationship lighthouseRelationship = model.getRelationship(deltaRelationship);
		    	switch (event.getType()) {
		    	case ADD:
					new LighthouseModelManagerPersistence(model).addEvent(event);
		    		mapFireRelEvent.put(deltaRelationship, event.getType());
		    		break;
		    	case REMOVE:
		    		event.setArtifact(lighthouseRelationship);
		    		new LighthouseModelManagerPersistence(model).addEvent(event);			
		    		mapFireRelEvent.put(lighthouseRelationship, event.getType());
		    		break;
		    	}
		    }
	    }
	    
		// Fire Modifications, call the UI
		fireClassModifications(mapFireClassEvent);
		fireRelationshipModifications(mapFireRelEvent);
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
