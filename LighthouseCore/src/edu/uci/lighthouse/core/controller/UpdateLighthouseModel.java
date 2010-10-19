package edu.uci.lighthouse.core.controller;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.log4j.Logger;

import edu.uci.lighthouse.core.Activator;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseEvent.TYPE;
import edu.uci.lighthouse.model.LighthouseInterface;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseModelUtil;
import edu.uci.lighthouse.model.LighthouseRelationship;

public class UpdateLighthouseModel {

	private static Logger logger = Logger.getLogger(UpdateLighthouseModel.class);

	private LighthouseModel model;

	public UpdateLighthouseModel(LighthouseModel model) {
		this.model = model;
	}

	/**Does not save in database, only in the lighthouse model*/
	public void updateEvents(Collection<LighthouseEvent> listEvents) {
		LighthouseModelManager LhManager = new LighthouseModelManager(model);
		Collection<String> listClassesToRemove = new LinkedHashSet<String>(); 
		// for each entity event
		for (LighthouseEvent event : listEvents) {
			Object artifact = event.getArtifact();
			if (artifact instanceof LighthouseEntity) {
				logger.debug("updating: " + event.toString());
				LhManager.addEvent(event);
				/**
				 * if I have an event to remove a class/interface AND this class is not on
				 * my workspace than I have to remove this class/interface from my model and
				 * hence from my visualization
				 */
				if (event.getType()==TYPE.REMOVE) {
					if (artifact instanceof LighthouseClass || artifact instanceof LighthouseInterface) {
						LighthouseEntity entity = (LighthouseEntity) artifact;
						String fqn = entity.getFullyQualifiedName();
						if ( event.getAuthor().equals(Activator.getDefault().getAuthor()) 
								|| (Controller.getInstance().getWorkingCopy().get(fqn)==null))  {
							listClassesToRemove.add(fqn);
						}
					}
				}
			} else if (artifact instanceof LighthouseRelationship) {
				if (event.getType()==TYPE.REMOVE) {
					LighthouseRelationship rel = (LighthouseRelationship) artifact;
					if (rel.getType()!=LighthouseRelationship.TYPE.INSIDE) {
						LhManager.removeRelationship(rel);
					}
				}
			}
		}
		// for each relationship event
		for (LighthouseEvent event : listEvents) {
			Object artifact = event.getArtifact();
			if (artifact instanceof LighthouseRelationship) {
				logger.debug("updating: " + event.toString());
				if (event.getType()==TYPE.ADD) {
					LhManager.addEvent(event);	
				}
			}
		}
		LhManager.removeArtifactsAndEvents(listClassesToRemove);
	}

	public Collection<LighthouseEvent> updateCommittedEvents(List<String> listClazzFqn, Date svnCommittedTime, LighthouseAuthor author) {
		Collection<LighthouseEvent> listEvents = LighthouseModelUtil.getEventsInside(model, listClazzFqn); 
		LinkedHashSet<LighthouseEvent> listEventsToCommitt = new LinkedHashSet<LighthouseEvent>();
		for (LighthouseEvent event : listEvents) {
			if (event.getAuthor().equals(author)) {
				if (!event.isCommitted()) {
					event.setCommitted(true);
					event.setCommittedTime(svnCommittedTime);
					listEventsToCommitt.add(event);
				}
			}
		}
		return listEventsToCommitt;
	}
	
}
