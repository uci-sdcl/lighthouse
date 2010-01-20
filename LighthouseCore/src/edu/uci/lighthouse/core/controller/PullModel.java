package edu.uci.lighthouse.core.controller;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseModelUtil;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.LHEventDAO;

public class PullModel {

	private LighthouseModel model;
	
	private static Logger logger = Logger.getLogger(PullModel.class);
	
	public PullModel(LighthouseModel lighthouseModel) {
		this.model = lighthouseModel;
		
	}
	
	/**
	 * Timeout procedure will get all new events (timestamp > lastDBaccessTime)
	 * @param lastDBaccessTime Last time that we accessed the database
	 * @return 
	 * @throws JPAException 
	 * */
	public List<LighthouseEvent>  getNewEventsFromDB(Date lastDBaccessTime, LighthouseAuthor author) throws JPAException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("timestamp", lastDBaccessTime);
		parameters.put("author", author);
		List<LighthouseEvent> listEvents = new LHEventDAO().executeNamedQuery("LighthouseEvent.findByTimestamp", parameters);
		LighthouseModelManager modelManager = new LighthouseModelManager(model);
		for (LighthouseEvent event : listEvents) {
			modelManager.addEvent(event);
		}
		return listEvents;
	}
	
	public Collection<LighthouseEvent> executeQueryCheckout(HashMap<String, Date> mapClassFqnToLastRevisionTimestamp) throws JPAException {
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
						if (!LighthouseModelUtil.wasEventRemoved(listEvents,event,null)) {
							modelManager.addArtifact(artifact);
							eventsToFire.add(event);
						}
					}
				} else {
					if (artifact instanceof LighthouseRelationship) {
						if (!LighthouseModelUtil.isValidRelationship((LighthouseRelationship) artifact, listEntitiesInside)) {
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
