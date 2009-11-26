package edu.uci.lighthouse.core.controller;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseModelUtil;
import edu.uci.lighthouse.model.LighthouseRelationship;
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
		updateLighthouseModel(listEvents);
		return listEvents;
	}
	
	public Collection<LighthouseEvent> loadModel(HashMap<String, Date> mapClassFqnToLastRevisionTimestamp) {
		return executeQueryAfterCheckout(mapClassFqnToLastRevisionTimestamp);
	}
	
	public Collection<LighthouseEvent> executeQueryAfterCheckout(HashMap<String, Date> mapClassFqnToLastRevisionTimestamp) {
		HashMap<String,Date> mapEntityTime = new HashMap<String,Date>();
		LighthouseModelManager modelManager = new LighthouseModelManager(model);
		LinkedHashSet<LighthouseEntity> listEntitiesInside = new LinkedHashSet<LighthouseEntity>();
		for (Map.Entry<String, Date> entry : mapClassFqnToLastRevisionTimestamp.entrySet()) {
			String fqnClazz = entry.getKey();
			Date revisionTimestamp = entry.getValue();
			listEntitiesInside = modelManager.selectEntitiesInsideClass(fqnClazz);
			mapEntityTime.put(fqnClazz, revisionTimestamp);
			for (LighthouseEntity fromEntity : listEntitiesInside) {
				mapEntityTime.put(fromEntity.getFullyQualifiedName(), revisionTimestamp);
			}
		}
		List<LighthouseEvent> listEvents = new LHEventDAO().executeQueryCheckOut(mapEntityTime);
		LinkedHashSet<LighthouseEvent> eventsToFire = new LinkedHashSet<LighthouseEvent>();
		for (LighthouseEvent event : listEvents) {
			Object artifact = event.getArtifact();
			Date revisionTime = getArtifactRevisionTime(artifact,mapEntityTime);
			if (event.isCommitted() && 	
				(	(revisionTime.after(event.getCommittedTime())
					|| revisionTime.equals(event.getCommittedTime()))
				) ) {
				if (event.getType()==LighthouseEvent.TYPE.ADD) {
					if (!LighthouseModelUtil.wasEventRemoved(listEvents,event)) {
						modelManager.addArtifact(artifact);
						eventsToFire.add(event);
					}
				}
			} else {
				if (artifact instanceof LighthouseRelationship) {
					if (!LighthouseModelUtil.isValidRelationship(artifact, listEntitiesInside)) {
						continue; // do NOT add relationship
					}
				}
				modelManager.addEvent(event);
				eventsToFire.add(event);
			}			
		}
		return eventsToFire;
	}

	private Date getArtifactRevisionTime(Object artifact, HashMap<String, Date> mapEntityTime) {
		Date revisionTime = null;
		if (artifact instanceof LighthouseEntity) {
			LighthouseEntity entity = (LighthouseEntity) artifact;
			revisionTime = mapEntityTime.get(entity.getFullyQualifiedName());
		} else if (artifact instanceof LighthouseRelationship) {
			LighthouseRelationship rel = (LighthouseRelationship) artifact;
			revisionTime = mapEntityTime.get(rel.getFromEntity().getFullyQualifiedName());
		}
		if (revisionTime==null) {
			logger.error("There is one artifact without revisionTime: " + artifact);
			revisionTime = new Date(0);
		}
		return revisionTime;
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
