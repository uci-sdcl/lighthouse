package edu.uci.lighthouse.core.controller;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

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

	public void updateCommittedEvents(List<String> listClazzFqn, Date svnCommittedTime, String authorName) throws JPAUtilityException {
		new LHEventDAO().updateCommittedEvents(listClazzFqn,svnCommittedTime, authorName);
	}
	
}
