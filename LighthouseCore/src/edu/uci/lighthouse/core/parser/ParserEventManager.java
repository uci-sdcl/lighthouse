package edu.uci.lighthouse.core.parser;

import org.apache.log4j.Logger;

import edu.uci.lighthouse.model.LighthouseAbstractModel;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseFile;
import edu.uci.lighthouse.model.LighthouseFileManager;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.jpa.JPAUtilityException;
import edu.uci.lighthouse.model.jpa.LHEntityDAO;
import edu.uci.lighthouse.model.util.LHPreference;

public class ParserEventManager {
	
	private static Logger logger = Logger.getLogger(ParserEventManager.class);

	public static LighthouseEntity addEntity(LighthouseAbstractModel model,
			LighthouseEntity entity) throws JPAUtilityException {		
		if (model instanceof LighthouseModel) {
			LighthouseEvent event = new LighthouseEvent(LighthouseEvent.TYPE.ADD,LHPreference.author,entity);
			new LighthouseModelManager((LighthouseModel) model).addEvent(event);
			return (LighthouseEntity) event.getArtifact();
		} else if (model instanceof LighthouseFile) {
			return new LighthouseFileManager((LighthouseFile) model).addEntity(entity);
		}
		logger.warn("Could not find instance of model: " + model);
		return null;
	}

	public static LighthouseRelationship addRelationship(LighthouseAbstractModel model,
			LighthouseRelationship relationship) throws JPAUtilityException {
		if (model instanceof LighthouseModel) {
			LighthouseEvent event = new LighthouseEvent(LighthouseEvent.TYPE.ADD,LHPreference.author,relationship);
			new LighthouseModelManager((LighthouseModel) model).addEvent(event);
			return (LighthouseRelationship) event.getArtifact();
		} else if (model instanceof LighthouseFile) {
			return new LighthouseFileManager((LighthouseFile) model).addRelationship(relationship);
		}
		logger.warn("Could not find instance of model: " + model);
		return null;
	}

	public static LighthouseEntity getEntityFromDataBase(String fqnTo) {
		return new LHEntityDAO().get(fqnTo);
	}
	
}
