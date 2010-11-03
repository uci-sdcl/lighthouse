package edu.uci.lighthouse.core.controller;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

import edu.uci.lighthouse.core.Activator;
import edu.uci.lighthouse.core.parser.LighthouseParser;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.LHEventDAO;
import edu.uci.lighthouse.parser.ParserException;

/**
 * This is a facade used to send events to the database
 */
public class PushModel {
	
	private LighthouseModel model;
	
	private static PushModel instance;
	
	private PushModel(LighthouseModel model) {
		this.model = model;
	}
	
	public static PushModel getInstance() {
		if (instance == null){
			instance = new PushModel(LighthouseModel.getInstance());
		}
		return instance;
	}
	
	public void saveEventsInDatabase(Collection<LighthouseEvent> listEvents) throws JPAException {
		LHEventDAO dao = new LHEventDAO();
		dao.saveListEvents(listEvents,null);		
	}
	
	public void importEventsToDatabase(Collection<LighthouseEvent> listEvents, IProgressMonitor monitor) throws JPAException {
		LHEventDAO dao = new LHEventDAO();
		dao.saveListEvents(listEvents,monitor);
	}
	
	public Collection<LighthouseEvent> uploadJavaFilesToDatabase(Collection<IFile> javaFiles)
			throws ParserException, JPAException {
		if (javaFiles.size() > 0) {
			LighthouseParser parser = new LighthouseParser();
			parser.execute(javaFiles);
			Collection<LighthouseRelationship> listLighthouseRel = parser.getListRelationships();
			Collection<LighthouseEntity> listEntities = parser.getListEntities();
			//FIXME (nilmax): Verificar bloco comentado com ele.
			// set SVN time as newDate(1969-12-31 16:00:00)
//			Map<String, Date> mapClassToSVNCommittedTime = Controller.getInstance().getWorkingCopy();
//			for (LighthouseEntity entity : listEntities) {
//				if (entity instanceof LighthouseClass 
//						|| entity instanceof LighthouseInterface) {
//					mapClassToSVNCommittedTime.put(entity.getFullyQualifiedName(), new Date(0));
//				}
//			}
			LighthouseModelManager modelManager = new LighthouseModelManager(model);
			Collection<LighthouseEvent> listEvents = modelManager
					.createEventsAndSaveInLhModel(Activator.getDefault()
							.getAuthor(), listEntities, listLighthouseRel);
			return listEvents;
		}
		return null;
	}

}
