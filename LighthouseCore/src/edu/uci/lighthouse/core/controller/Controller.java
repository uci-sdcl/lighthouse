package edu.uci.lighthouse.core.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.osgi.framework.BundleContext;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNInfo;
import org.tigris.subversion.svnclientadapter.SVNClientException;

import edu.uci.lighthouse.core.Activator;
import edu.uci.lighthouse.core.dbactions.DatabaseActionsBuffer;
import edu.uci.lighthouse.core.dbactions.IDatabaseAction;
import edu.uci.lighthouse.core.dbactions.pull.CheckoutAction;
import edu.uci.lighthouse.core.dbactions.pull.SynchronizeModelAction;
import edu.uci.lighthouse.core.dbactions.pull.UpdateAction;
import edu.uci.lighthouse.core.dbactions.push.CommitAction;
import edu.uci.lighthouse.core.dbactions.push.FileEventAction;
import edu.uci.lighthouse.core.listeners.IJavaFileStatusListener;
import edu.uci.lighthouse.core.listeners.IPluginListener;
import edu.uci.lighthouse.core.listeners.ISVNEventListener;
import edu.uci.lighthouse.core.parser.LighthouseParser;
import edu.uci.lighthouse.core.preferences.DatabasePreferences;
import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.core.util.WorkbenchUtility;
import edu.uci.lighthouse.core.widgets.StatusWidget;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseEvent.TYPE;
import edu.uci.lighthouse.model.LighthouseFile;
import edu.uci.lighthouse.model.LighthouseFileManager;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.io.IPersistence;
import edu.uci.lighthouse.model.io.PersistenceService;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.JPAUtility;
import edu.uci.lighthouse.parser.ParserException;
import edu.uci.lighthouse.services.LighthouseServiceFactory;
import edu.uci.lighthouse.services.persistence.IPersistenceService;
import edu.uci.lighthouse.services.persistence.PersistenceException;

public class Controller implements ISVNEventListener, IJavaFileStatusListener,
IPluginListener, Runnable, IPropertyChangeListener {

	private static Logger logger = Logger.getLogger(Controller.class);
	private WorkingCopy mapClassToSVNCommittedTime = new WorkingCopy();
	/*
	 * After the file get the changes from the repository, the event 'change'
	 * will be throw and we can wrong interpret the new changes being
	 * modifications made by the user. We need this list to ignore those files.
	 */
	private Collection<IFile> ignorefilesJustUpdated = new LinkedHashSet<IFile>();
	private boolean threadRunning;
	private final int threadTimeout = 20000;
	private DatabaseActionsBuffer databaseActionsBuffer = new DatabaseActionsBuffer();
	
	private static final String workspaceMetadata = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString()  + "/.metadata/";
	private static final String modelFileName = workspaceMetadata + "lighthouse-model.bin";
	private static final String deltaFileName = workspaceMetadata + "lighthouse-delta.bin";
	private static final String TEMP_RESOURCE_LHPreviousFile = "TEMP_RESOURCE_LHPreviousFile.java";

	private static Controller instance;

	private Controller () {
		// Singleton pattern
	}

	public Map<String, Date> getWorkingCopy() {
		return mapClassToSVNCommittedTime;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(
				this);
		loadPreferences();
		loadModel();
		//loadDelta();
		logger.info("Starting thread...");
		(new Thread(this)).start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		threadRunning = false;
		Activator.getDefault().getPreferenceStore()
		.removePropertyChangeListener(this);
		savePreferences();
		saveModel();
		//saveDelta();
	}

	private void loadDelta() {
		try {
			ObjectInputStream ois = new ObjectInputStream(
					new FileInputStream(deltaFileName));
			logger.debug("loading " + deltaFileName);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private void saveDelta() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(deltaFileName));
			oos.close();
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	public void loadPreferences() {
		logger.info("loadPreferences()");
		String prefix = Activator.PLUGIN_ID;
		IPreferenceStore prefStore = Activator.getDefault()
		.getPreferenceStore();

		Date timestampLastEventReceived = new Date(prefStore.getLong(prefix + "lastDBAccess"));
		TimestampLastEventReceived.getInstance().setValue(timestampLastEventReceived);
		logger.debug("loading lastDBAccess=" + timestampLastEventReceived);
		
		IPersistenceService svc = (IPersistenceService) LighthouseServiceFactory.getService("GenericPersistenceService");
		try {
			mapClassToSVNCommittedTime = (WorkingCopy) svc.load(mapClassToSVNCommittedTime);
		} catch (PersistenceException e) {
			logger.error(e, e);
		}
	}

	public void savePreferences() {
		String prefix = Activator.PLUGIN_ID;
		IPreferenceStore prefStore = Activator.getDefault()
		.getPreferenceStore();
		
		Date timestampLastEventReceived = TimestampLastEventReceived.getInstance().getValue();;
		prefStore.setValue(prefix + "lastDBAccess", timestampLastEventReceived.getTime());
		logger.debug("saving lastDBAccess=" + timestampLastEventReceived);

		IPersistenceService svc = (IPersistenceService) LighthouseServiceFactory.getService("GenericPersistenceService");
		try {
			svc.save(mapClassToSVNCommittedTime);
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	private void loadModel() {
		logger.info("loadModel()");
		try {
			IPersistence mos = PersistenceService.getService(LighthouseModel.getInstance(),IPersistence.BINARY);
			mos.load(modelFileName);
		} catch (Exception e) {
			logger.error(e);
		}

		if (LighthouseModel.getInstance().isEmpty()
				&& mapClassToSVNCommittedTime.size() > 0) {
			PullModel pullModel = new PullModel(LighthouseModel.getInstance());
			try {
				pullModel.executeQueryCheckout(mapClassToSVNCommittedTime);
			} catch (JPAException e) {
				logger.error(e, e);
				// UserDialog.openError(e.getMessage());
			}
		}
		// Show the events in the UI.
		WorkbenchUtility.updateProjectIcon();
		LighthouseModel.getInstance().fireModelChanged();
	}

	private void saveModel() {
		try {
			IPersistence mos = PersistenceService.getService(LighthouseModel.getInstance(),IPersistence.BINARY);
			mos.save(modelFileName);
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	@Override
	public void run() {
		threadRunning = true;
		while (threadRunning) {
			// Sleep for the time defined by thread timeout
			try {
				refreshModelBasedOnLastEventFetched();
				while (!databaseActionsBuffer.isEmpty()) {
					IDatabaseAction databaseAction = databaseActionsBuffer.peek();
					databaseAction.run();
					databaseActionsBuffer.poll();
				}
			} catch (JPAException e) {
				logger.error(e, e);
			}
			try {
				Thread.sleep(threadTimeout);
			} catch (InterruptedException e) {
				logger.error(e, e);
			}
		}
	};

	/**
	 * Refresh the LighthouseModel with new events from database and fire this
	 * changes to the UI.
	 * 
	 * @throws JPAException
	 */
	public synchronized void refreshModelBasedOnLastEventFetched()
	throws JPAException {
		/*
		 * If the map's size == 0, it means that it is the first time that user
		 * is running Lighthouse. Then, the LighthouseModel will be updated only
		 * if the user execute a checkout first.
		 */
		if (mapClassToSVNCommittedTime.size() != 0) {
			LighthouseAuthor author = Activator.getDefault().getAuthor();
			LighthouseModel lhModel = LighthouseModel.getInstance();
			PullModel pullModel = new PullModel(lhModel);
			Date timestampLastEventReceived = TimestampLastEventReceived.getInstance().getValue();
			List<LighthouseEvent> events = pullModel.getNewEventsFromDB(
					timestampLastEventReceived, author);
			new UpdateLighthouseModel(lhModel).updateEvents(events);
			fireModificationsToUI(events);
			if (!events.isEmpty()) {
				logger.debug("timeout[ " + timestampLastEventReceived + " ] brought: ["
						+ events.size() + "] events");
				timestampLastEventReceived = getLatestTime(events); 
			}
		}
	}

	public static Date getLatestTime(Collection<LighthouseEvent> events) {
		Date latestTime = new Date(0);
		for (LighthouseEvent event : events) {
			Date timestamp = event.getTimestamp();
			Date committedTime = event.getCommittedTime();
			Date time = committedTime.after(timestamp) ? committedTime : timestamp;
			latestTime = (time.after(latestTime)) ? time : latestTime;
		}
		return latestTime;
	}

	
	@Override
	public void open(final IFile iFile, boolean hasErrors) {
		// TODO Do Nothing :S
	}

	@Override
	public void close(IFile iFile, boolean hasErrors) {
		// TODO Do Nothing :S
	}

	@Override
	public void change(final IFile iFile, boolean hasErrors) {
		// verify if any of the classWithErrors still have errors
		// than parse the files that does not have errors anymore
		// and remove the classes from the list classWithErrors
		if (ModelUtility.belongsToImportedProjects(iFile, false)) {
			if (ignorefilesJustUpdated.contains(iFile)) {
				ignorefilesJustUpdated.remove(iFile);
				// event change was invoked _withOUT_ a merge problem
				// Update base version - the delta does not matter
				// it is not working because we are getting the old IFile
				// not the new one generateDeltaFromBaseVersion(Collections.singleton(iFile),true);
				// I will suppose that the revisionTime was set in the update event
			} else {
				try {
					IFile previousIFile = getPreviousVersion(iFile);
					if (previousIFile!=null) {
						generateDeltaAndSaveIntoModel(previousIFile, iFile);
						removeIFile(previousIFile);
					}
				} catch (Exception e) {
					logger.error(e, e);
				}
			}
		}
	}

	private void generateDeltaAndSaveIntoModel(IFile previousIFile, IFile currentIFile) {
		try {
			LighthouseFile previousLhFile = parseIFile(previousIFile);
			LighthouseFile currentLhFile = parseIFile(currentIFile);

			LighthouseDelta delta = new LighthouseDelta(
					Activator.getDefault().getAuthor(),
					previousLhFile,
					currentLhFile);

			LinkedHashSet<LighthouseEvent> deltaEvents = delta.getEvents();
 
			if (deltaEvents.size() == 0) {
				logger.error("We have an workspace event without delta elements (delta==0)");
			} else {
				logger.debug("Workspace event generated delta events: "+ deltaEvents.size());
			}
			LighthouseModel lhModel = LighthouseModel.getInstance();
			new UpdateLighthouseModel(lhModel).updateEvents(deltaEvents);
			fireModificationsToUI(deltaEvents);
			
			FileEventAction fileEventAction = new FileEventAction(deltaEvents);
			databaseActionsBuffer.offer(fileEventAction);
		} catch (ParserException e) {
			logger.error(e, e);		
		} 
	}

	private LighthouseFile parseIFile(IFile iFile) throws ParserException {
		if (iFile!=null) {
			LighthouseParser parser = new LighthouseParser();
			parser.execute(Collections.singleton(iFile));

			LighthouseFile lhFile = new LighthouseFile();
			LighthouseFileManager lhFileManager = new LighthouseFileManager(lhFile);

			lhFileManager.populateLHFile(
					parser.getListEntities(), 
					parser.getListRelationships());
			return lhFile;
		} else {
			return null;
		}
	}

	/**
	 * The method add happens when a class is added in the workspace or when is
	 * checkout a project in a empty workspace.
	 */
	@Override
	public void add(IFile iFile, boolean hasErrors) {
		if (ModelUtility.belongsToImportedProjects(iFile, false)) {
			final String classFqn = ModelUtility.getClassFullyQualifiedName(iFile);
			if (!mapClassToSVNCommittedTime.containsKey(classFqn)) {
				// We have to put the classFqn in this map in order to show the event in the UI.
				mapClassToSVNCommittedTime.put(classFqn, new Date(0));
				try {
					generateDeltaAndSaveIntoModel(null, iFile);
				} catch (Exception e) {
					logger.error(e, e);
				}
			}
		}
	}

	@Override
	public void remove(IFile iFile, boolean hasErrors) {
		if (ModelUtility.belongsToImportedProjects(iFile, false)) {
			// FIXME:URGENT Gambis pra pegar FQN pelo caminho do arquivo. Melhorar
			// depois usando o source folder do projeto
			String srcFolder = "/src/";
			String projectName = iFile.getProject().getName();
			int index = iFile.getFullPath().toOSString().indexOf(projectName);
			String classFqn = iFile.getFullPath().toOSString().substring(
					index + projectName.length() + srcFolder.length())
					.replaceAll("/", ".").replaceAll(".java", "");
			classFqn = projectName + "." + classFqn;
			try {
				IFile previousIFile = getPreviousVersion(iFile);
				if (previousIFile != null) {
					generateDeltaAndSaveIntoModel(previousIFile, null);
					mapClassToSVNCommittedTime.remove(classFqn);
					removeIFile(previousIFile);
				}
			} catch (Exception e) {
				logger.error(e, e);
			}
		}
	}

	
	@Override
	public void checkout(Map<IFile, ISVNInfo> svnFiles) {
		// Ignore these files inside the change event, once we don't want to
		// generate deltas for other people changes.
		// ignoredFiles.addAll(svnFiles.keySet());
		WorkingCopy workingCopy = getWorkingCopy(svnFiles, true);
		mapClassToSVNCommittedTime.putAll(workingCopy);
		CheckoutAction checkoutAction = new CheckoutAction(workingCopy);
		databaseActionsBuffer.offer(checkoutAction);
	}

	@Override
	public void update(Map<IFile, ISVNInfo> svnFiles) {
		// Ignore in the change event these files, because we don't want to
		// generate deltas for other people changes.
		ignorefilesJustUpdated.addAll(svnFiles.keySet());
		WorkingCopy workingCopy = getWorkingCopy(svnFiles, false);
		mapClassToSVNCommittedTime.putAll(workingCopy);
		UpdateAction updateAction = new UpdateAction(workingCopy);
		databaseActionsBuffer.offer(updateAction);
	}

	@Override
	public void commit(Map<IFile, ISVNInfo> svnFiles) {
		HashMap<String, Date> workingCopy = getWorkingCopy(svnFiles, false);
		mapClassToSVNCommittedTime.putAll(workingCopy);
		LighthouseModelManager modelManager = new LighthouseModelManager(
				LighthouseModel.getInstance());
		modelManager.removeCommittedEventsAndArtifacts(workingCopy.keySet());

		// assuming that there is just one committed time
		ISVNInfo[] svnInfo = svnFiles.values().toArray(new ISVNInfo[0]);
		Date svnCommittedTime = svnInfo[0].getLastChangedDate();

		LighthouseModel lhModel = LighthouseModel.getInstance();
		UpdateLighthouseModel updateLighthouseModel = new UpdateLighthouseModel(lhModel);

		Collection<LighthouseEvent> listEventsToCommitt = 
			updateLighthouseModel.updateCommittedEvents(
					ModelUtility.getClassesFullyQualifiedName(svnFiles),
					svnCommittedTime, 
					Activator.getDefault().getAuthor());

		fireModificationsToUI(listEventsToCommitt);

		logger.debug("Committed [" + listEventsToCommitt.size() + "] events "
				+ "with SVN time: " + svnCommittedTime);
		
		CommitAction commitAction = new CommitAction(listEventsToCommitt);
		databaseActionsBuffer.offer(commitAction);
	}

	private WorkingCopy getWorkingCopy(Map<IFile, ISVNInfo> svnFiles, boolean checkDatabase) {
		WorkingCopy result = new WorkingCopy();
		for (Entry<IFile, ISVNInfo> entry : svnFiles.entrySet()) {
			if (ModelUtility.belongsToImportedProjects(entry.getKey(), checkDatabase)) {
				String fqn = ModelUtility.getClassFullyQualifiedName(entry.getKey());
				if (fqn != null) {
					ISVNInfo svnInfo = entry.getValue();
					result.put(fqn, svnInfo.getLastChangedDate());
				}
			}
		}
		return result;
	}

	@Override
	public void conflict(Map<IFile, ISVNInfo> svnFiles) {
		// FIXME DO Nothing :S
	}
	
	private void fireModificationsToUI(Collection<LighthouseEvent> events) {
		logger.debug("fireModificationsToUI ("+events.size()+" events)");
		// We need hashmap to avoid repaint the UI multiple times
		HashMap<LighthouseClass, LighthouseEvent.TYPE> mapClassEvent = new HashMap<LighthouseClass, LighthouseEvent.TYPE>();
		HashMap<LighthouseRelationship, LighthouseEvent.TYPE> mapRelationshipEvent = new HashMap<LighthouseRelationship, LighthouseEvent.TYPE>();
		LighthouseModel model = LighthouseModel.getInstance();
		for (LighthouseEvent event : events) {
			Object artifact = event.getArtifact();
			// TODO: comment more this method. It is confusing.
			// ADD creates a new class node in the view and populates it
			// MODIFY just re-populates the class node
			if (artifact instanceof LighthouseClass) {
				LighthouseClass klass = (LighthouseClass) artifact;
				mapClassEvent.put(klass, event.getType());
			} else if (artifact instanceof LighthouseEntity) {
				LighthouseModelManager manager = new LighthouseModelManager(
						model);
				LighthouseClass klass = manager
				.getMyClass((LighthouseEntity) artifact);
				if (klass != null) {
					// Never overwrite the ADD event
					if (!mapClassEvent.containsKey(klass)) {
						// Just refresh the class node
						mapClassEvent.put(klass, LighthouseEvent.TYPE.MODIFY);
					}
				}
			} else if (artifact instanceof LighthouseRelationship) {
				LighthouseRelationship relationship = (LighthouseRelationship) artifact;
				mapRelationshipEvent.put(relationship, event.getType());
			}
		}
		// Fire class changes to the UI
		for (Entry<LighthouseClass, TYPE> entry : mapClassEvent.entrySet()) {
			logger.debug("Firing class: "+entry.getKey());
			model.fireClassChanged(entry.getKey(), entry.getValue());
		}
		// Fire relationship changes to the UI
		for (Entry<LighthouseRelationship, TYPE> entry : mapRelationshipEvent
				.entrySet()) {
			logger.debug("Firing relationship: "+entry.getKey());
			model.fireRelationshipChanged(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		try {
			//FIXME: for a better looking code
			if (event.getProperty().indexOf(DatabasePreferences.ROOT) != -1) {
				JPAUtility.canConnect(DatabasePreferences
						.getDatabaseSettings());
				StatusWidget.getInstance().setStatus(Status.OK_STATUS);
				synchronizeModelWithDatabase(true);
//				if (threadSuspended) {
//					synchronized (this) {
//						StatusWidget.getInstance().setStatus(Status.OK_STATUS);
//						threadSuspended = false;
//						notify();
//					}
//				}
			}
		} catch (Exception e) {
			logger.error(e, e);
			StatusWidget.getInstance().setStatus(Status.CANCEL_STATUS);
		}
	}

	public void synchronizeModelWithDatabase(){
		synchronizeModelWithDatabase(false);
	}

	private void synchronizeModelWithDatabase(final boolean forceNewConnection){
		mapClassToSVNCommittedTime = getWorkingCopyFromWorkspace();
		SynchronizeModelAction synchronizeModelAction = new SynchronizeModelAction(mapClassToSVNCommittedTime);
		databaseActionsBuffer.offer(synchronizeModelAction);
	}

// FIXME I can remove this code (I want to see the JOB code)
//	private void synchronizeModelWithDatabase(final boolean forceNewConnection){
//		final Job job = new Job("Synchronizing Model...") {
//			@Override
//			protected IStatus run(IProgressMonitor monitor) {
//				try {
//					monitor.beginTask("Synchronizing model with database...", IProgressMonitor.UNKNOWN);
//					if (forceNewConnection){
//						JPAUtility.initializeEntityManagerFactory(DatabasePreferences
//								.getDatabaseSettings());
//					}
//					mapClassToSVNCommittedTime = getWorkingCopyFromWorkspace();
//					LighthouseModel.getInstance().clear();
//					checkoutWorkingCopy(mapClassToSVNCommittedTime);
//					WorkbenchUtility.updateProjectIcon();
//					//LighthouseModel.getInstance().fireModelChanged();
//				} catch (JPAException e) {
//					logger.error(e,e);
//					UserDialog.openError("JPAException: "+e.getMessage());
//				} finally {
//					monitor.done();
//				}
//				return Status.OK_STATUS;
//			}
//		};
//		job.setUser(true);
//		job.schedule();
//	}

	private WorkingCopy getWorkingCopyFromWorkspace(){
		WorkingCopy result = new WorkingCopy();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject[] projects = workspace.getRoot().getProjects();
		try {
			ISVNClientAdapter svnAdapter = SVNProviderPlugin.getPlugin()
			.getSVNClient();
			for (IProject project : projects) {
				if (project.isOpen()) {
					try {
						IJavaProject jProject = (IJavaProject) project
						.getNature(JavaCore.NATURE_ID);
						if (jProject != null) {
							Collection<IFile> iFiles = WorkbenchUtility
							.getFilesFromJavaProject(jProject);
							for (IFile iFile : iFiles) {
								try {
									ISVNInfo svnInfo = svnAdapter
									.getInfoFromWorkingCopy(iFile
											.getLocation().toFile());
									String fqn = ModelUtility
									.getClassFullyQualifiedName(iFile);
									if (fqn != null) {
										Date revision = svnInfo
										.getLastChangedDate();
										if (revision == null) {
											revision = new Date(0);
										} else {
											revision = new Date(revision
													.getTime());
										}
										result.put(fqn, revision);
									}
								} catch (SVNClientException ex1) {
									logger.error(ex1);
								}
							}
						}
					} catch (CoreException e) {
						logger.error(e, e);
					}

				}
			}
		} catch (SVNException ex) {
			logger.error(ex, ex);
		}
		return result;
	}

	public static Controller getInstance(){
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}

	public IFile getPreviousVersion(IFile from) throws Exception{
		String tempWorkspaceResource = from.getFullPath().toOSString();
		String fileShortName = from.getFullPath().toFile().getName();
		tempWorkspaceResource = tempWorkspaceResource.replaceAll(fileShortName, TEMP_RESOURCE_LHPreviousFile);

		IFileState[] history = from.getHistory(null);
		if (history.length==0) {
			return null;
		}
		InputStream inputStream = history[0].getContents();

		IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(tempWorkspaceResource));

		IProject project = from.getProject();
		project.getFile(tempWorkspaceResource);
		iFile.create(inputStream, true, null);

		return iFile;
	}

	private void removeIFile(IFile iFile) throws CoreException {
		iFile.delete(true, null);
	}

}
