package edu.uci.lighthouse.core.controller;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.osgi.framework.BundleContext;
import org.tigris.subversion.svnclientadapter.ISVNInfo;

import edu.uci.lighthouse.core.Activator;
import edu.uci.lighthouse.core.listeners.IJavaFileStatusListener;
import edu.uci.lighthouse.core.listeners.IPluginListener;
import edu.uci.lighthouse.core.listeners.ISVNEventListener;
import edu.uci.lighthouse.core.parser.IParserAction;
import edu.uci.lighthouse.core.parser.LighthouseParser;
import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseFile;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.jpa.JPAUtilityException;
import edu.uci.lighthouse.model.jpa.LHEventDAO;

public class Controller implements ISVNEventListener, IJavaFileStatusListener, IPluginListener{
	
	private static Logger logger = Logger.getLogger(Controller.class);
	
	private HashMap<String, Date> mapClassFqnToLastRevisionTimestamp = new HashMap<String, Date>();
	
	// ....
	private Queue<Map<IFile, ISVNInfo>> pendingWorkingCopyModifications = new LinkedList<Map<IFile, ISVNInfo>>();
	
	private HashMap<String, LighthouseFile> classBaseVersion = new HashMap<String, LighthouseFile>();
	
	private List<String> classWithErrors = new LinkedList<String>();
	
	private Date lastDBAccess = null;
	
	@Override
	public void start(BundleContext context) throws Exception {
		loadPreferences();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		savePreferences();
	}
	
	public void loadPreferences(){
		String prefix = Activator.getDefault().PLUGIN_ID;
		IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
		
		lastDBAccess = new Date(prefStore.getLong(prefix+"lastDBAccess"));
		logger.debug("loading lastDBAccess="+lastDBAccess);
	}
	
	public void savePreferences(){
		String prefix = Activator.getDefault().PLUGIN_ID;
		IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
		
		prefStore.setValue(prefix+"lastDBAccess", lastDBAccess.getTime());
		logger.debug("saving lastDBAccess="+lastDBAccess);
	}
	
	public synchronized void refreshModelBasedOnLastDBAccess() {
		LHEventDAO evtDao = new LHEventDAO();
		lastDBAccess = evtDao.getCurrentTimestamp();
		
		/*
		 * If the map's size = 0, it means that it is the first time that user
		 * is running Lighthouse. Then, the LighthouseModel will be updated only
		 * if the user execute a checkout first.
		 */
		if (mapClassFqnToLastRevisionTimestamp.size() == 0) {
			PullModel pullModel = new PullModel(LighthouseModel.getInstance());
			pullModel.executeQueryTimeout(lastDBAccess);
		}	
	}
	
	public synchronized void refreshModelBasedOnWorkingCopy(){		
		if (pendingWorkingCopyModifications.size() > 0){
			Map<IFile, ISVNInfo> svnFiles = pendingWorkingCopyModifications.poll();
			mapClassFqnToLastRevisionTimestamp = refreshWorkingCopy(svnFiles); 
			
			PullModel pullModel = new PullModel(LighthouseModel.getInstance());
			pullModel.executeQueryAfterCheckout(mapClassFqnToLastRevisionTimestamp);
		}
	}
		
	@Override
	public void change(IFile iFile, boolean hasErrors) {
//		refreshModelBasedOnWorkingCopy();
		
		final String classFqn = getClassFullyQualifiedName(iFile);
		
		if (hasErrors){
			//FIXME: criar LighthouseFile a partir do Modelo
			classWithErrors.add(classFqn);
		} else {
			try {
				final LighthouseFile currentLhFile = new LighthouseFile();
				LighthouseParser parser = new LighthouseParser();
				parser.executeInAJob(currentLhFile, Collections.singleton(iFile), new IParserAction(){
					@Override
					public void doAction() {
						LighthouseFile baseLhFile = classBaseVersion.get(classFqn);
						LighthouseDelta delta = new LighthouseDelta(Activator.getDefault().getAuthor(),baseLhFile,currentLhFile);
						
						PushModel pushModel = new PushModel(LighthouseModel.getInstance());
						try {
							pushModel.updateModelFromDelta(delta);
						} catch (JPAUtilityException e) {
							//TODO: Try to throw up this exception
							logger.error(e);
						}
						
						// FIXME: Fire model events to show in the LH view
						// LOOK into PushModel
						
					}
				});
			} catch (Exception e) {
				logger.error(e);
			}			
		}		
	}

	@Override
	public void close(IFile iFile, boolean hasErrors) {
//		refreshModelBasedOnWorkingCopy();
		final String classFqn = getClassFullyQualifiedName(iFile);
		classBaseVersion.remove(classFqn);
	}

	@Override
	public void open(final IFile iFile, boolean hasErrors) {
//		refreshModelBasedOnWorkingCopy();
		
		final String classFqn = getClassFullyQualifiedName(iFile);
		
		if (hasErrors){
			//FIXME: criar LighthouseFile a partir do Modelo
			classWithErrors.add(classFqn);
		} else {
			try {
				final LighthouseFile lhFile = new LighthouseFile();
				LighthouseParser parser = new LighthouseParser();
				parser.executeInAJob(lhFile, Collections.singleton(iFile), new IParserAction(){
					@Override
					public void doAction() {
						classBaseVersion.put(classFqn, lhFile);
					}
				});
			} catch (Exception e) {
				logger.error(e);
			}			
		}
	}

	@Override
	public void checkout(Map<IFile, ISVNInfo> svnFiles) {	
		pendingWorkingCopyModifications.offer(svnFiles);
		
		// Debug purposes only
		List<String> cNames = getClassesFullyQualifiedName(svnFiles);
		logger.debug("checkout fqns:"+cNames);
	}

	@Override
	public void commit(Map<IFile, ISVNInfo> svnFiles) {
		refreshWorkingCopy(svnFiles);
		
		PushModel pushModel = new PushModel(LighthouseModel.getInstance());
		pushModel.updateCommittedEvents(getClassesFullyQualifiedName(svnFiles), Activator.getDefault().getAuthor().getName());		
		refreshModelBasedOnLastDBAccess(); // Refresh is needed because, to cleanup the committed events
		
		// FIXME: Fire model events to show in the LH view
	}
	
	@Override
	public void update(Map<IFile, ISVNInfo> svnFiles) {
		pendingWorkingCopyModifications.offer(svnFiles);
		
		
		// FIXME: mudar base lighhousefile
		
//		refreshModelBasedOnWorkingCopy(svnFiles);
		
		// FIXME: Fire model events to show in the LH view
	}

	private HashMap<String, Date> refreshWorkingCopy(Map<IFile, ISVNInfo> svnFiles) {
		for (Entry<IFile, ISVNInfo> entry : svnFiles.entrySet()) {
			String fqn = getClassFullyQualifiedName(entry.getKey());
			if (fqn != null){
				ISVNInfo svnInfo = svnFiles.get(entry.getValue());
				mapClassFqnToLastRevisionTimestamp.put(fqn, svnInfo.getLastChangedDate());
			}
		}
		return mapClassFqnToLastRevisionTimestamp;
	}
	
	private List<String> getClassesFullyQualifiedName(Map<IFile, ISVNInfo> svnFiles) {
		LinkedList<String> result = new LinkedList<String>();
		for (IFile iFile : svnFiles.keySet()) {
			String fqn = getClassFullyQualifiedName(iFile);
			if (fqn != null){
				result.add(fqn);
			}
		}
		return result;
	}

	public String getClassFullyQualifiedName(IFile iFile) {
		String result = null;
		try {
			ICompilationUnit icu = JavaCore.createCompilationUnitFrom(iFile);
			IType[] types = icu.getTypes();
			for (IType iType : types) {
				String fileNameWithoutExtension = iFile.getName().replaceAll(
						".java", "");
				String className = iType.getFullyQualifiedName().replaceAll(
						"\\w+\\.", "");
				if (fileNameWithoutExtension.equals(className)) {
					// Java files should have at least one class with the same
					// name of the file
					result = iType.getFullyQualifiedName();
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}
}

