package edu.uci.lighthouse.model;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import edu.uci.lighthouse.model.jpa.JPAUtilityException;
import edu.uci.lighthouse.model.jpa.LHEntityDAO;
import edu.uci.lighthouse.model.jpa.LHEventDAO;
import edu.uci.lighthouse.model.jpa.LHRelationshipDAO;

public class LighthouseModelManagerPersistence extends LighthouseModelManager {

	public LighthouseModelManagerPersistence(LighthouseModel model) {
		super(model);
	}

	@Override
	public void addEvent(LighthouseEvent event) throws JPAUtilityException {
		super.addEvent(event);
		saveEventIntoDatabase(event);
	}

	public void saveAllIntoDataBase() throws JPAUtilityException {
		for (LighthouseEvent event : this.model.getListEvents()) {
			saveEventIntoDatabase(event);
		}
	}
	
	private void saveEventIntoDatabase(LighthouseEvent event) throws JPAUtilityException {
//		event.setTimestamp(new Date()); // FIXME get time from database
		new LHEventDAO().save(event);
	}
	
	public LighthouseEntity getEntity(String fqn) {
		LighthouseEntity entity = super.getEntity(fqn);
		if (entity==null) {
			entity = new LHEntityDAO().get(fqn);
		}
		return entity;
	}
	
	public LinkedHashSet<LighthouseEntity> getEntitiesInsideClass(List<String> listClazzFqn) {
		LinkedHashSet<LighthouseEntity> listFromEntities = new LinkedHashSet<LighthouseEntity>();
		for (String clazzFqn : listClazzFqn) {
			listFromEntities.addAll(getEntitiesInsideClass(clazzFqn));			
		}
		return listFromEntities;
	}
	
	public  List<LighthouseEntity> getEntitiesInsideClass(String clazzFqn) {
		LighthouseEntity clazz = new LighthouseModelManagerPersistence(model).getEntity(clazzFqn);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("relType", LighthouseRelationship.TYPE.INSIDE);
		parameters.put("toEntity", clazz);
		return new LHRelationshipDAO().executeNamedQueryGetFromEntityFqn("LighthouseRelationship.findFromEntityByTypeAndToEntity", parameters);
	}
	
}
