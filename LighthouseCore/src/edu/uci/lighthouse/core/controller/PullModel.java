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
import edu.uci.lighthouse.model.jpa.JPAUtilityException;
import edu.uci.lighthouse.model.jpa.LHEventDAO;

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
	
	public List<LighthouseEvent> executeQueryAfterCheckout(HashMap<String, Date> mapClassFqnToLastRevisionTimestamp) {
		HashMap<String,Date> mapEntityTime = new HashMap<String,Date>();
		// for each class - get all entities that is INSIDE it (from the database)
		for (Map.Entry<String, Date> entry : mapClassFqnToLastRevisionTimestamp.entrySet()) {
			String fqnClazz = entry.getKey();
			Date commitTimestamp = entry.getValue();
			List<LighthouseEntity> listFromEntities = new LighthouseModelManagerPersistence(model).getEntitiesInsideClass(fqnClazz);
			mapEntityTime.put(fqnClazz, commitTimestamp);
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
