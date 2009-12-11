package edu.uci.lighthouse.core.controller;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

import edu.uci.lighthouse.core.Activator;
import edu.uci.lighthouse.core.parser.LighthouseParser;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseInterface;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseModelUtil;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.jpa.JPAUtilityException;
import edu.uci.lighthouse.model.jpa.LHEventDAO;
import edu.uci.lighthouse.parser.ParserException;

public class PushModel {
	
	private static Logger logger = Logger.getLogger(PushModel.class);

	private LighthouseModel model;
	
	public PushModel(LighthouseModel model) {
		this.model = model;
	}
	
	public void updateModelFromDelta(LighthouseDelta delta) throws JPAUtilityException {   
	    updateModelFromEvents(delta.getEvents());
	}

	public void updateModelFromEvents(Collection<LighthouseEvent> listEvents) throws JPAUtilityException {   
	    LighthouseModelManager LhManager = new LighthouseModelManager(model);
	    // for each entity event
	    for (LighthouseEvent event : listEvents) {
			Object artifact = event.getArtifact();
			if (artifact instanceof LighthouseEntity) {
				logger.debug("updating: " + event.toString());
				LhManager.addEvent(event);
			}			
		}
	    // for each relationship event
	    for (LighthouseEvent event : listEvents) {
			Object artifact = event.getArtifact();
		    if (artifact instanceof LighthouseRelationship) {
		    	logger.debug("updating: " + event.toString());
		    	LhManager.addEvent(event);
		    }
	    }
	    saveEventsInDatabase(listEvents,null);
	}
	
	public Collection<LighthouseEvent> updateCommittedEvents(List<String> listClazzFqn, Date svnCommittedTime, LighthouseAuthor author) throws JPAUtilityException {
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
		new LHEventDAO().updateCommittedEvents(listEventsToCommitt, svnCommittedTime);
		return listEventsToCommitt;
	}
	
	public Collection<LighthouseEvent> ParseJavaFiles(Collection<IFile> javaFiles)
			throws ParserException, JPAUtilityException {
		if (javaFiles.size() > 0) {
			LighthouseParser parser = new LighthouseParser();
			parser.execute(javaFiles);
			Collection<LighthouseEntity> listEntities = parser.getListEntities();
			// set SVN time as newDate(1969-12-31 16:00:00)
			Map<String, Date> mapClassToSVNCommittedTime = Controller.getWorkingCopy();
			for (LighthouseEntity entity : listEntities) {
				if (entity instanceof LighthouseClass 
						|| entity instanceof LighthouseInterface) {
					mapClassToSVNCommittedTime.put(entity.getFullyQualifiedName(), new Date(0));
				}
			}
			Collection<LighthouseRelationship> listLighthouseRel = parser.getListRelationships();
			LighthouseModelManager modelManager = new LighthouseModelManager(model);
			Collection<LighthouseEvent> listEvents = modelManager
					.createEventsAndSaveInLhModel(Activator.getDefault()
							.getAuthor(), listEntities, listLighthouseRel);
//			model.fireModelChanged();
			return listEvents;
		}
		return null;
	}

	public void saveEventsInDatabase(final Collection<LighthouseEvent> listEvents,	IProgressMonitor monitor) throws JPAUtilityException {
		LHEventDAO dao = new LHEventDAO();
		dao.saveListEvents(listEvents,monitor);		
	}
	
}
