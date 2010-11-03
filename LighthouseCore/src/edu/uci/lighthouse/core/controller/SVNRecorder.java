package edu.uci.lighthouse.core.controller;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.tigris.subversion.svnclientadapter.ISVNInfo;

import edu.uci.lighthouse.core.Activator;
import edu.uci.lighthouse.core.listeners.ISVNEventListener;
import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.LHRepositoryEventDAO;
import edu.uci.lighthouse.model.repository.LighthouseRepositoryEvent;

public class SVNRecorder implements ISVNEventListener {

	private static Logger logger = Logger.getLogger(SVNRecorder.class);
	
	@Override
	public void checkout(Map<IFile, ISVNInfo> svnFiles) {
		try {
			// Assure we are recording just files those were imported previously.
			Map<IFile, ISVNInfo> files = new HashMap<IFile, ISVNInfo>();
			for (Entry<IFile, ISVNInfo> entry : svnFiles.entrySet()) {
				if (ModelUtility.belongsToImportedProjects(entry.getKey(), false)){
					files.put(entry.getKey(), entry.getValue());
				}
			}
			saveRepositoryEvent(files, LighthouseRepositoryEvent.TYPE.CHECKOUT,
					new Date());
		} catch (JPAException e) {
			logger.error(e,e);
		}
	}

	@Override
	public void commit(Map<IFile, ISVNInfo> svnFiles) {
		try {
			// Assure we are recording just files those were imported previously.
			Map<IFile, ISVNInfo> files = new HashMap<IFile, ISVNInfo>();
			for (Entry<IFile, ISVNInfo> entry : svnFiles.entrySet()) {
				if (ModelUtility.belongsToImportedProjects(entry.getKey(), false)){
					files.put(entry.getKey(), entry.getValue());
				}
			}
			saveRepositoryEvent(files, LighthouseRepositoryEvent.TYPE.CHECKIN,
					new Date());
		} catch (JPAException e) {
			logger.error(e,e);
		}
	}

	@Override
	public void update(Map<IFile, ISVNInfo> svnFiles) {
		try {
			// Assure we are recording just files those were imported previously.
			Map<IFile, ISVNInfo> files = new HashMap<IFile, ISVNInfo>();
			for (Entry<IFile, ISVNInfo> entry : svnFiles.entrySet()) {
				if (ModelUtility.belongsToImportedProjects(entry.getKey(), false)){
					files.put(entry.getKey(), entry.getValue());
				}
			}
			saveRepositoryEvent(files, LighthouseRepositoryEvent.TYPE.UPDATE,
					new Date());
		} catch (JPAException e) {
			logger.error(e,e);
		}
	}

	@Override
	public void conflict(Map<IFile, ISVNInfo> svnFiles) {
		try {
			// Assure we are recording just files those were imported previously.
			Map<IFile, ISVNInfo> files = new HashMap<IFile, ISVNInfo>();
			for (Entry<IFile, ISVNInfo> entry : svnFiles.entrySet()) {
				if (ModelUtility.belongsToImportedProjects(entry.getKey(), false)){
					files.put(entry.getKey(), entry.getValue());
				}
			}
			saveRepositoryEvent(files, LighthouseRepositoryEvent.TYPE.CONFLICT,
					new Date());
		} catch (JPAException e) {
			logger.error(e,e);
		}
	}

	private void saveRepositoryEvent(Map<IFile, ISVNInfo> svnFiles, 
			LighthouseRepositoryEvent.TYPE type,
			Date eventTime
			) throws JPAException {

		/* We make this to navigate the class names and their
		 * properties in the same order when calling 
		 * methods getClassesFullyQualifiedName and
		 * getFilesRevisionNumbers
		 */
		IFile [] iFiles = svnFiles.keySet().toArray(new IFile[0]);
		List<String> classNames = getClassesFullyQualifiedNameInOrder(svnFiles, iFiles); 
		LighthouseAuthor author = Activator.getDefault().getAuthor(); 
		List<Number> versionsAffected = getFilesRevisionNumbersInOrder(svnFiles, iFiles);
		
		// If there is not a version number for each class name, return
		if (classNames.size() != versionsAffected.size())
			return;
		
		Collection<LighthouseRepositoryEvent> listRepEvents = new LinkedList<LighthouseRepositoryEvent>();
		for (int i = 0; i < classNames.size(); i++) {
			LighthouseRepositoryEvent repEvent = new LighthouseRepositoryEvent(author,type,classNames.get(i),eventTime,versionsAffected.get(i).longValue());
			listRepEvents.add(repEvent);
		}
		LHRepositoryEventDAO dao = new LHRepositoryEventDAO();
		dao.saveRepositoryEvents(listRepEvents);
	}
	
	private List<String> getClassesFullyQualifiedNameInOrder(
			Map<IFile, ISVNInfo> svnFiles, IFile [] iFiles) {
		LinkedList<String> result = new LinkedList<String>();
		for (IFile iFile : iFiles) {
			String fqn = ModelUtility.getClassFullyQualifiedName(iFile);
			if (fqn != null) {
				result.add(fqn);
			}
		}
		return result;
	}

	private List<Number> getFilesRevisionNumbersInOrder(
			Map<IFile, ISVNInfo> svnFiles, IFile [] iFiles) {
		LinkedList<Number> result = new LinkedList<Number>();
		for (IFile iFile : iFiles) {
			Number svnRevisionNumber = svnFiles.get(iFile).getRevision().getNumber();
			if (svnRevisionNumber != null) {
				result.add(svnRevisionNumber);
			}
		}
		return result;
	}
	
}
