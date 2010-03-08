package edu.uci.lighthouse.core.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.tigris.subversion.svnclientadapter.ISVNInfo;

import edu.uci.lighthouse.core.listeners.ISVNEventListener;
import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.jpa.JPAException;
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
			new PushModel(LighthouseModel.getInstance()).saveRepositoryEvent(
					files, LighthouseRepositoryEvent.TYPE.CHECKOUT,
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
			new PushModel(LighthouseModel.getInstance()).saveRepositoryEvent(
					files, LighthouseRepositoryEvent.TYPE.CHECKIN,
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
			new PushModel(LighthouseModel.getInstance()).saveRepositoryEvent(
					files, LighthouseRepositoryEvent.TYPE.UPDATE,
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
			new PushModel(LighthouseModel.getInstance()).saveRepositoryEvent(
					files, LighthouseRepositoryEvent.TYPE.CONFLICT,
					new Date());
		} catch (JPAException e) {
			logger.error(e,e);
		}
	}

}
