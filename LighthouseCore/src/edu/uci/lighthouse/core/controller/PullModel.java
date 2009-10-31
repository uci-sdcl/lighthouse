package edu.uci.lighthouse.core.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseModelManagerPersistence;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.jpa.JPAUtilityException;
import edu.uci.lighthouse.model.jpa.LHEventDAO;
import edu.uci.lighthouse.model.jpa.LHRelationshipDAO;

public class PullModel {

	private LighthouseModel model;
	
	
	public PullModel(LighthouseModel lighthouseModel) {
		this.model = lighthouseModel;
		
	}
	
	public void run() throws JPAUtilityException {
		// TODO Start Thread
		
		List<LighthouseEvent> listEvents = executeQueryTimeout(new Date());
		updateLighthouseModel(listEvents);
		
		// TODO Fire the Visualization
		
	}

	private void updateLighthouseModel(List<LighthouseEvent> listEvents) throws JPAUtilityException {
		for (LighthouseEvent event : listEvents) {
			new LighthouseModelManager(model).addEvent(event);
		}
	}

	/**
	 * Timeout procedure will get all new events (timestamp > lastDBaccessTime)
	 * @param lastDBaccessTime Last time that we accessed the database
	 * */
	public List<LighthouseEvent> executeQueryTimeout(Date lastDBaccessTime) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("timestamp", lastDBaccessTime);
		return new LHEventDAO().executeNamedQuery("LighthouseEvent.findByTimestamp", parameters);
	}
	
	public List<LighthouseEvent> executeQueryEventAfterCheckout() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		HashMap<String,Date> mapEntityTime = new HashMap<String,Date>();
		// for each class - get all entities that is INSIDE it (from the database), and set the commitTimestamp
		for (Map.Entry<String, Date> entry : model.getCheckedOutEntities().getEntrySet()) {
			String fqnClazz = entry.getKey();
			LighthouseEntity clazz = new LighthouseModelManagerPersistence(model).getEntity(fqnClazz);
			Date commitTimestamp = entry.getValue();
			
			parameters.put("relType", LighthouseRelationship.TYPE.INSIDE);
			parameters.put("toEntity", clazz);
			List<LighthouseEntity> listFromEntities = new LHRelationshipDAO().executeNamedQueryGetFromEntityFqn("LighthouseRelationship.findFromEntityByTypeAndToEntity", parameters);
			
			mapEntityTime.put(fqnClazz, commitTimestamp);
			// set commitTimestamp for each entity inside a class
			for (LighthouseEntity fromEntity : listFromEntities) {
				mapEntityTime.put(fromEntity.getFullyQualifiedName(), commitTimestamp);
			}
		}
		return new LHEventDAO().executeQueryEntitiesAndTime(mapEntityTime); 
	}
	
//	private void checkOutProcedure() {
//		
//	}
//
//	private void getAllEventsFromDatabase() {
//		List<LighthouseEvent> listEventDatabse = LHEventController.getInstance().list();
//	}

}
