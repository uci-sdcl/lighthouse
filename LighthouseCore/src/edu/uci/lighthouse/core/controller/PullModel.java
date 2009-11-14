package edu.uci.lighthouse.core.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseEvent.TYPE;
import edu.uci.lighthouse.model.jpa.LHEventDAO;

public class PullModel {

	private LighthouseModel model;
	
	private static Logger logger = Logger.getLogger(PullModel.class);
	
	public PullModel(LighthouseModel lighthouseModel) {
		this.model = lighthouseModel;
		
	}
	
	private void updateLighthouseModel(List<LighthouseEvent> listEvents) {
		if (listEvents.size() > 0){
			logger.debug("Updating model: "+listEvents);
		}
		// Update the model
		LighthouseModelManager modelManager = new LighthouseModelManager(model);
		for (LighthouseEvent event : listEvents) {
			modelManager.addEvent(event);
		}
	}

	/**
	 * Timeout procedure will get all new events (timestamp > lastDBaccessTime)
	 * @param lastDBaccessTime Last time that we accessed the database
	 * @return 
	 * */
	public List<LighthouseEvent>  getNewEventsFromDB(Date lastDBaccessTime) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("timestamp", lastDBaccessTime);
		List<LighthouseEvent> listEvents = new LHEventDAO().executeNamedQuery("LighthouseEvent.findByTimestamp", parameters);
		
		// Update the model
		updateLighthouseModel(listEvents);
		
		return listEvents;
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
		
		// Update the model
		updateLighthouseModel(listEvents);
		
		removeCommittedEvents(listEvents,mapEntityTime);
		
		return listEvents;
	}
	
	public List<LighthouseEvent> loadModel(HashMap<String, Date> mapClassFqnToLastRevisionTimestamp) {
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

		List<LighthouseEvent> listEvents = new LHEventDAO().list();
		// Update the model
		updateLighthouseModel(listEvents);
		
		removeCommittedEvents(listEvents,mapEntityTime);
		
		return listEvents;
	}
	
	
	
	public void removeCommittedEvents(List<LighthouseEvent> listEvents, HashMap<String,Date> mapEntityTime) {
		// Remove Committed events (TYPE==ADD and committedTime before revisionTime) from the model
		LighthouseModelManager modelManager = new LighthouseModelManager(model);
		for (LighthouseEvent event : listEvents) {
			Object artifact = event.getArtifact();
			if (artifact instanceof LighthouseEntity) {
				LighthouseEntity entity = (LighthouseEntity) artifact;
				Date revisionTime = mapEntityTime.get(entity.getFullyQualifiedName());
				if (revisionTime!=null) {
					if (event.getType()==TYPE.ADD
							&& event.isCommitted()
							&& (revisionTime.after(event.getCommittedTime())
									|| revisionTime.equals(event.getCommittedTime())) ) {
						modelManager.removeEvent(event);
					}
				}
			}
		}
	}
	
}
