package edu.uci.lighthouse.core.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseEvent.TYPE;
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

	private void updateLighthouseModel(List<LighthouseEvent> listEvents) {
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
		LighthouseModelManager modelManager = new LighthouseModelManager(model);
		for (Map.Entry<String, Date> entry : mapClassFqnToLastRevisionTimestamp.entrySet()) {
			String fqnClazz = entry.getKey();
			Date revisionTimestamp = entry.getValue();
			List<LighthouseEntity> listFromEntities = modelManager.getEntitiesInsideClass(fqnClazz);
			mapEntityTime.put(fqnClazz, revisionTimestamp);
			for (LighthouseEntity fromEntity : listFromEntities) {
				mapEntityTime.put(fromEntity.getFullyQualifiedName(), revisionTimestamp);
			}
		}
		
		List<LighthouseEvent> listEvents = new LHEventDAO().executeQueryEntitiesAndTime(mapEntityTime);
		
		for (LighthouseEvent event : listEvents) {
			modelManager.addEvent(event);
		}
		
		removeCommittedEvents(listEvents,mapEntityTime);
		
		return listEvents;
	}
	
	private void removeCommittedEvents(List<LighthouseEvent> listEvents, HashMap<String,Date> mapEntityTime) {
		// Remove Committed events (TYPE==ADD and committedTime before revisionTime) from the model
		for (LighthouseEvent event : listEvents) {
			Object artifact = event.getArtifact();
			if (artifact instanceof LighthouseEntity) {
				LighthouseEntity entity = (LighthouseEntity) artifact;
				Date revisionTime = mapEntityTime.get(entity.getFullyQualifiedName());
				if (event.getType()==TYPE.ADD
					&& event.isCommitted()
					&& event.getCommittedTime().before(revisionTime)) {
					new LighthouseModelManager(model).removeEvent(event);
				}
			}
		}
	}
	
}
