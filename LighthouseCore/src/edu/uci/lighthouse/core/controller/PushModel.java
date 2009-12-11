package edu.uci.lighthouse.core.controller;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import edu.uci.lighthouse.core.Activator;
import edu.uci.lighthouse.core.parser.LighthouseParser;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
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
	    jobToSaveEvents(listEvents);
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
	
	public Collection<LighthouseEvent> importJavaFiles(Collection<IFile> javaFiles)
			throws ParserException, JPAUtilityException {
		if (javaFiles.size() > 0) {
			LighthouseParser parser = new LighthouseParser();
			parser.execute(javaFiles);
			Collection<LighthouseEntity> listEntities = parser
					.getListEntities();
			Collection<LighthouseRelationship> listLighthouseRel = parser
					.getListRelationships();
			LighthouseModelManager modelManager = new LighthouseModelManager(
					model);
			Collection<LighthouseEvent> listEvents = modelManager
					.createEventsAndSaveInModel(Activator.getDefault()
							.getAuthor(), listEntities, listLighthouseRel);
			jobToSaveEvents(listEvents);
			return listEvents;
		}
		return null;
	}
	
	public void jobToSaveEvents(final Collection<LighthouseEvent> listEvents) throws JPAUtilityException {
		final Job job = new Job("Saving to the database...") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try{ 
				monitor.beginTask("Total", listEvents.size());
				saveEventsInDatabase(listEvents, monitor);
				if (monitor.isCanceled()) return Status.CANCEL_STATUS;
				} catch (JPAUtilityException e) {
					return Status.CANCEL_STATUS;
				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}
		};
		job.addJobChangeListener(new JobChangeAdapter() {
	        public void done(IJobChangeEvent event) {
	        	Shell shell = PlatformUI.getWorkbench()
	    		.getActiveWorkbenchWindow().getShell();
	        if (event.getResult().isOK())
	        	MessageDialog.openInformation(shell,"Lighthouse", "Project imported successfully!");
	           else
	        	MessageDialog.openError(shell,"Database Connection", "Imposible to connect to server. Please, check your connection settings.");
	        }
	     });
		job.setUser(true);
		job.schedule();
	}

	private void saveEventsInDatabase(final Collection<LighthouseEvent> listEvents,	IProgressMonitor monitor) throws JPAUtilityException {
		LHEventDAO dao = new LHEventDAO();
		dao.saveListEvents(listEvents,monitor);		
	}
	
}
