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
	
	public Collection<LighthouseEvent> executeQueryCheckout(HashMap<String, Date> mapClassFqnToLastRevisionTimestamp) {
		logger.info("GET IN: PullModel.executeQueryCheckout()");
		LighthouseModelManager modelManager = new LighthouseModelManager(model);
		LinkedHashSet<LighthouseEntity> listEntitiesInside = new LinkedHashSet<LighthouseEntity>();
		LinkedHashSet<LighthouseEvent> eventsToFire = new LinkedHashSet<LighthouseEvent>();
		for (Map.Entry<String, Date> entry : mapClassFqnToLastRevisionTimestamp.entrySet()) {
			String fqnClazz = entry.getKey();
			Date revisionTimestamp = entry.getValue();
			listEntitiesInside = modelManager.selectEntitiesInsideClass(fqnClazz);
			List<LighthouseEvent> listEvents = new LHEventDAO().executeQueryCheckOut(listEntitiesInside, revisionTimestamp);
			logger.debug("fqnClazz: " + fqnClazz + "  revisionTimestamp: " + revisionTimestamp + " listEntitiesInside: " + listEntitiesInside.size() + " listEvents: " + listEvents.size());
			for (LighthouseEvent event : listEvents) {
				Object artifact = event.getArtifact();
				if (event.isCommitted() && 	
					(	(revisionTimestamp.after(event.getCommittedTime())
						|| revisionTimestamp.equals(event.getCommittedTime()))
					) ) {
					if (event.getType()==LighthouseEvent.TYPE.ADD) {
						if (!LighthouseModelUtil.wasCommittedEventRemoved(listEvents,event)) {
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
		}
		logger.info("GET OUT: PullModel.executeQueryCheckout()");
		return eventsToFire;
	}

}
