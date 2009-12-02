package edu.uci.lighthouse.core.controller;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;

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
	    LhManager.saveEventsIntoDatabase(listEvents);
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
	
	public void importEclipseProjectToDatabase(IWorkspace workspace, Collection<String> listEclipseProject) throws ParserException, JPAUtilityException {
		IProject[] projects = workspace.getRoot().getProjects();
		final Collection<IFile> files = new LinkedList<IFile>();
		for (IProject project : projects) {
			if (listEclipseProject.contains(project.getName())) {
				if (project.isOpen()) {
					files.addAll(getFilesFromProject(project));
				}
			}
		}
		if (files.size() > 0) {
			LighthouseParser parser = new LighthouseParser();
			parser.execute(files);
			Collection<LighthouseEntity> listEntities = parser.getListEntities();
			Collection<LighthouseRelationship> listLighthouseRel = parser.getListRelationships();
			LighthouseModelManager modelManager = new LighthouseModelManager(model);
			Collection<LighthouseEvent> listEvents = modelManager.createEventsAndSaveInModel(Activator.getDefault()
					.getAuthor(), listEntities, listLighthouseRel);
			modelManager.saveEventsIntoDatabase(listEvents);
		}
	}
	
	private Collection<IFile> getFilesFromProject(IProject project) {
		final Collection<IFile> files = new HashSet<IFile>();
		try {
			project.getFolder("src").accept(new IResourceVisitor() {
				@Override
				public boolean visit(IResource resource) throws CoreException {
					if (resource.getType() == IResource.FILE
							&& resource.getFileExtension().equalsIgnoreCase(
									"java")) {
						files.add((IFile) resource);
						return false;
					} else {
						return true;
					}
				}
			});
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return files;
	}
	
}
