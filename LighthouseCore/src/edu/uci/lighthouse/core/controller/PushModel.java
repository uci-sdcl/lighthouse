package edu.uci.lighthouse.core.controller;

import java.util.LinkedHashSet;
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

	public void updateCommittedEvents(List<String> listClazzFqn, String authorName) throws JPAUtilityException {
//		LinkedHashSet<LighthouseEntity> listEntity = new LighthouseModelManager(model).getEntitiesInsideClass(listClazzFqn);
//		String command = 	"UPDATE LighthouseEvent e " +
//							"SET e.isCommitted = 1 " + " , " +
//							"e.committedTime = CURRENT_TIMESTAMP " +
//							"WHERE ( e.author.name = " + "'" + authorName + "'" + " " +
//							"AND e.isCommitted = 0 ) AND ";
//		command+= " ( ";
//		for (LighthouseEntity entity : listEntity) {
//			String fqn = entity.getFullyQualifiedName();
//			command+= " ( e.entity.fullyQualifiedName = " + fqn + " ) ";
//			command+= " OR ";
//			command+= " ( e.relationship.primary.from = " + fqn + " OR " + "e.relationship.prymary.to = " + fqn + " ) ";
//		}
//		command+= " ) ";
//		new LHEventDAO().executeUpdateQuery(command);
		new LHEventDAO().updateCommittedEvents(listClazzFqn,authorName);
	}
	
}
