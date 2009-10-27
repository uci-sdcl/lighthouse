package edu.uci.lighthouse.core.parser;

import edu.uci.lighthouse.model.LighthouseAbstractModel;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseFile;
import edu.uci.lighthouse.model.LighthouseFileManager;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.jpa.LHEntityController;
import edu.uci.lighthouse.model.util.Preference;

public class ParserEventManager {

	public static LighthouseEntity addEntity(LighthouseAbstractModel model,
			LighthouseEntity entity) {		
		if (model instanceof LighthouseModel) {
			LighthouseEvent event = new LighthouseEvent(LighthouseEvent.TYPE.ADD,Preference.author,entity);
			new LighthouseModelManager((LighthouseModel) model).addEvent(event);
			return (LighthouseEntity) event.getArtifact();
		} else if (model instanceof LighthouseFile) {
			return new LighthouseFileManager((LighthouseFile) model).addEntity(entity);
		}
		return null; // TODO LOG ERROR
	}

	public static LighthouseRelationship addRelationship(LighthouseAbstractModel model,
			LighthouseRelationship relationship) {
		if (model instanceof LighthouseModel) {
			LighthouseEvent event = new LighthouseEvent(LighthouseEvent.TYPE.ADD,Preference.author,relationship);
			new LighthouseModelManager((LighthouseModel) model).addEvent(event);
			return (LighthouseRelationship) event.getArtifact();
		} else if (model instanceof LighthouseFile) {
			return new LighthouseFileManager((LighthouseFile) model).addRelationship(relationship);
		}
		return null; // TODO LOG ERROR
	}

	public static LighthouseEntity getEntityFromDataBase(String fqnTo) {
		return LHEntityController.getInstance().get(fqnTo);
	}
	
}
