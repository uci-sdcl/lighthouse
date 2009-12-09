package edu.uci.lighthouse.core.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.osgi.framework.BundleContext;
import org.tigris.subversion.svnclientadapter.ISVNInfo;

import edu.uci.lighthouse.core.Activator;
import edu.uci.lighthouse.core.listeners.IJavaFileStatusListener;
import edu.uci.lighthouse.core.listeners.IPluginListener;
import edu.uci.lighthouse.core.listeners.ISVNEventListener;
import edu.uci.lighthouse.core.parser.IParserAction;
import edu.uci.lighthouse.core.parser.JavaCompilerUtil;
import edu.uci.lighthouse.core.parser.LighthouseParser;
import edu.uci.lighthouse.model.BuildLHBaseFile;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseFile;
import edu.uci.lighthouse.model.LighthouseFileManager;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.LighthouseEvent.TYPE;
import edu.uci.lighthouse.model.io.IPersistence;
import edu.uci.lighthouse.model.io.LighthouseModelXMLPersistence;
import edu.uci.lighthouse.parser.ParserException;

public class Controller implements ISVNEventListener, IJavaFileStatusListener,
		IPluginListener, Runnable, IPropertyChangeListener {

	private static Logger logger = Logger.getLogger(Controller.class);
	private HashMap<String, Date> mapClassFqnToLastRevisionTimestamp = new HashMap<String, Date>();
	private HashMap<String, LighthouseFile> classBaseVersion = new HashMap<String, LighthouseFile>();
	private Set<IFile> classWithErrors = new LinkedHashSet<IFile>();
//	private Collection<IFile> ignoredFiles = new LinkedHashSet<IFile>(); // Used in update and checkout methods
	private Date lastDBAccess = null;
	private boolean threadRunning;
	private boolean threadSuspended;
	private final int threadTimeout = 5000;

	@Override
	public void start(BundleContext context) throws Exception {
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(this);
		
		loadPreferences();
		loadModel();
		(new Thread(this)).start();
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		threadSuspended = false;
		threadRunning = false;
		Activator.getDefault().getPreferenceStore().removePropertyChangeListener(this);
		savePreferences();
		saveModel();
	}

	@SuppressWarnings("unchecked")
	public void loadPreferences() {
		String prefix = Activator.PLUGIN_ID;
		IPreferenceStore prefStore = Activator.getDefault()
				.getPreferenceStore();

		lastDBAccess = new Date(prefStore.getLong(prefix + "lastDBAccess"));
		logger.debug("loading lastDBAccess=" + lastDBAccess);

		try {
			final String filename = "lighthouseWorkingCopy.version";
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					filename));
			mapClassFqnToLastRevisionTimestamp = (HashMap<String, Date>) ois
					.readObject();
			logger.debug("loading " + filename);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void savePreferences() {
		String prefix = Activator.PLUGIN_ID;
		IPreferenceStore prefStore = Activator.getDefault()
				.getPreferenceStore();

		prefStore.setValue(prefix + "lastDBAccess", lastDBAccess.getTime());
		logger.debug("saving lastDBAccess=" + lastDBAccess);

		try {
			final String filename = "lighthouseWorkingCopy.version";
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(filename));
			oos.writeObject(mapClassFqnToLastRevisionTimestamp);
			logger.debug("saving " + filename);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Refresh the LighthouseModel with new events from database and fire this
	 * changes to the UI.
	 */
	public synchronized void refreshModelBasedOnLastDBAccess() {
		/*
		 * If the map's size == 0, it means that it is the first time that user
		 * is running Lighthouse. Then, the LighthouseModel will be updated only
		 * if the user execute a checkout first.
		 */
		if (mapClassFqnToLastRevisionTimestamp.size() != 0) {
			PullModel pullModel = new PullModel(LighthouseModel.getInstance());
			List<LighthouseEvent> events = pullModel
					.getNewEventsFromDB(lastDBAccess);
			fireModificationsToUI(events);
			lastDBAccess = getTimestamp();
		}
	}

	private void loadModel() {
		IPersistence mos = new LighthouseModelXMLPersistence(LighthouseModel
				.getInstance());
		try {
			mos.load();
		} catch (Exception e) {
			logger.error(e);
		}

		if (LighthouseModel.getInstance().isEmpty()
				&& mapClassFqnToLastRevisionTimestamp.size() > 0) {
			PullModel pullModel = new PullModel(LighthouseModel.getInstance());
			Collection<LighthouseEvent> events = pullModel
					.executeQueryCheckout(mapClassFqnToLastRevisionTimestamp);
			fireModificationsToUI(events);
		}
	}
	
	private void saveModel(){
		try {
			IPersistence mos = new LighthouseModelXMLPersistence(LighthouseModel.getInstance());
			mos.save();
		} catch (IOException e) {
			logger.error(e);
		}
	}

	@Override
	public void open(final IFile iFile, boolean hasErrors) {

		/* This code works, however is not optimized. See the other block below. */

		final String classFqn = getClassFullyQualifiedName(iFile);
//		logger.debug("open " + classFqn);

//		if (hasErrors) {
//			classWithErrors.add(iFile);
//		}

		// if it is a NEW class
//		if (LighthouseModel.getInstance().getEntity(classFqn) == null) {
//			Collection<LighthouseEvent> deltaEvents = foo(Collections.singleton(iFile));
//			PushModel pushModel = new PushModel(
//					LighthouseModel.getInstance());
//			try {
//				pushModel.updateModelFromEvents(deltaEvents);
//			} catch (Exception e) {
//				// TODO: Try to throw up this exception
//				logger.error(e);
//			}
//			fireModificationsToUI(deltaEvents);
//			
//			mapClassFqnToLastRevisionTimestamp.put(classFqn, new Date(0));
//		} else {
			Thread task = new Thread() {
				@Override
				public void run() {
			//Removed the thread because the unopened files. When you try to guarantee the lifecycle (open,change,close) the base version can not
					LighthouseFile lhBaseFile = getBaseVersionFromDB(classFqn);
					classBaseVersion.put(classFqn, lhBaseFile);
				}
			};
			task.start();
//		}
		
		/* The code below is working. However we need to guarantee that the open event is ALWAYS fired for files that are open in the workspace whe eclipse is loading.*/
		
//		final String classFqn = getClassFullyQualifiedName(iFile);
//		logger.debug("open "+classFqn);
//			
//		if (hasErrors) {
//			Date revisionTime = mapClassFqnToLastRevisionTimestamp.get(classFqn);
//			if (revisionTime!= null) {
//				LighthouseFile lhBaseFile = BuildLHBaseFile.execute(LighthouseModel.getInstance(), classFqn, revisionTime, Activator.getDefault().getAuthor());
//				classBaseVersion.put(classFqn,lhBaseFile);
//			}
//			classWithErrors.add(iFile);
//		} else {
//				try {
//				final LighthouseFile lhFile = new LighthouseFile();
//				LighthouseParser parser = new LighthouseParser();
//				parser.executeInAJob(lhFile, Collections.singleton(iFile),
//				new IParserAction() {
//					@Override
//					public void doAction() {
//						logger.debug("base: " + classFqn
//								+ " LHFile entities:"
//								+ lhFile.getEntities().size());
//						classBaseVersion.put(classFqn, lhFile);
//						
//						// if it is a NEW class
//						if (LighthouseModel.getInstance().getEntity(classFqn) == null) {
//							LighthouseDelta delta = new LighthouseDelta(Activator.getDefault().getAuthor(),null,lhFile);
//							PushModel pushModel = new PushModel(LighthouseModel
//									.getInstance());
//							try {
//								pushModel.updateModelFromDelta(delta);
//							} catch (Exception e) {
//								// TODO: Try to throw up this exception
//								logger.error(e);
//							}
//							fireModificationsToUI(delta.getEvents());
//						}
//					}
//				});
//			} catch (Exception e) {
//				logger.error(e);
//			}
//		}
	}

	public LighthouseFile getBaseVersionFromDB(String classFullyQualifiedName) {
		LighthouseFile result = null;
		Date revisionTime = mapClassFqnToLastRevisionTimestamp.get(classFullyQualifiedName);
		if (revisionTime != null) {
			result = BuildLHBaseFile.execute(LighthouseModel.getInstance(),
					classFullyQualifiedName, revisionTime, Activator.getDefault().getAuthor());
		}
		return result;
	}

	@Override
	public void change(final IFile iFile, boolean hasErrors) {
		// verify if any of the classWithErrors still have errors
		// than parse the files that does not have errors anymore
		// and remove the classes from the list classWithErrors
		
		if (!hasErrors) {
//			classWithErrors.add(iFile);
//		} else {
			
//			if(ignoredFiles.contains(iFile)){
//				ignoredFiles.remove(iFile);
//			} else {	
				// Remove iFile from the list if it exists, since hasErrors = false
//				classWithErrors.remove(iFile);
//				Collection<IFile> filesWithoutErrors = getFilesWithoutErrors(classWithErrors);
//				classWithErrors.removeAll(filesWithoutErrors);
				
//				Collection<LighthouseEvent> deltaEvents = new LinkedList<LighthouseEvent>(); 
//				deltaEvents.addAll(generateDeltaFromBaseVersion(Collections.singleton(iFile)));
//				deltaEvents.addAll(generateDeltaFromBaseVersion(filesWithoutErrors));
			//////
			final String classFqn = getClassFullyQualifiedName(iFile);
			final LighthouseFile lhBaseFile = classBaseVersion.get(classFqn);
			// change will run only if we have base version 
			// That is a very good optimization
			if (lhBaseFile != null) { 
				Collection<LighthouseEvent> deltaEvents = generateDeltaFromBaseVersion(Collections
						.singleton(iFile));
				// TODO: Think about DB operations in a thread
				PushModel pushModel = new PushModel(LighthouseModel
						.getInstance());
				try {
					pushModel.updateModelFromEvents(deltaEvents);
				} catch (Exception e) {
					logger.error(e);
				}
				fireModificationsToUI(deltaEvents);
			}
//			}
			
//			final LinkedHashSet<LighthouseEvent> deltaEvents = new LinkedHashSet<LighthouseEvent>(); 
//			
//			// Use iterator to be able to remove the IFile in the loop
//			for (Iterator<IFile> itFile = classWithErrors.iterator(); itFile.hasNext();) {
//				IFile javaFile = itFile.next();
//				boolean hasError = JavaCompilerUtil.hasErrors(javaFile);
//				logger.debug("File: "+javaFile+" hasError: "+hasError);
//				if (!hasError){
//					itFile.remove();
//					
//					// TODO: Verify if it is suitable to put the try block in another method
//					try {
//						final String classFqn = getClassFullyQualifiedName(javaFile);
//						final LighthouseFile currentLhFile = new LighthouseFile();
//						LighthouseParser parser = new LighthouseParser();
//						parser.executeInAJob(currentLhFile, Collections
//								.singleton(iFile), new IParserAction() {
//							@Override
//							public void doAction() {
//								LighthouseFile lhBaseFile = classBaseVersion
//										.get(classFqn);
//								
//								if (lhBaseFile != null) {
//									LighthouseDelta delta = new LighthouseDelta(Activator
//											.getDefault().getAuthor(), lhBaseFile,
//											currentLhFile);
//									PushModel pushModel = new PushModel(LighthouseModel
//											.getInstance());
//									try {
//										pushModel.updateModelFromDelta(delta);
//									} catch (Exception e) {
//										logger.error(e);
//									}
//									// Updates the current base version
//									classBaseVersion.put(classFqn, currentLhFile);
//									deltaEvents.addAll(delta.getEvents());
//								}
//							}
//						});
//					} catch (Exception e) {
//						logger.error(e);
//					}
//				}
//			}
			
		}
	}
	
	private Collection<IFile> getFilesWithoutErrors(Collection<IFile> files){
		LinkedList<IFile> result = new LinkedList<IFile>();
		for (Iterator<IFile> itFile = files.iterator(); itFile.hasNext();) {
			IFile file = itFile.next();
			if (!JavaCompilerUtil.hasErrors(file)) {
				result.add(file);
			}
		}
		return result;
	}
	
	private Collection<LighthouseEvent> generateDeltaFromBaseVersion(Collection<IFile> files) {
		return generateDeltaFromBaseVersion(files,false);
	}
	
	private Collection<LighthouseEvent> generateDeltaFromBaseVersion(Collection<IFile> files, boolean force) {
		final List<LighthouseEvent> result = new LinkedList<LighthouseEvent>();

		// Use iterator to be able to remove the IFile in the loop
		for (IFile file : files) {
			final String classFqn = getClassFullyQualifiedName(file);
			final LighthouseFile lhBaseFile = classBaseVersion.get(classFqn);
			if (force || lhBaseFile != null) {
				try {
					final LighthouseParser parser = new LighthouseParser();
					parser.executeInAJob(Collections.singleton(file),
							new IParserAction() {
								@Override
								public void doAction() {
									LighthouseFile currentLhFile = new LighthouseFile();
									new LighthouseFileManager(currentLhFile)
											.buildLHFile(parser
													.getListEntities(), parser
													.getListRelationships());
									LighthouseDelta delta = new LighthouseDelta(
											Activator.getDefault().getAuthor(),
											lhBaseFile, currentLhFile);
									classBaseVersion.put(classFqn,
											currentLhFile);
									result.addAll(delta.getEvents());
								}
							});
				} catch (ParserException e) {
					logger.error(e);
				}
			}
		}
		return result;
	}

	@Override
	public void close(IFile iFile, boolean hasErrors) {
		final String classFqn = getClassFullyQualifiedName(iFile);
		if (hasErrors) {
			classWithErrors.add(iFile);
		} 
		classBaseVersion.remove(classFqn);
	}
	
	@Override
	public void checkout(Map<IFile, ISVNInfo> svnFiles) {
		// Ignore these files inside the change event, once we don't want to generate deltas for other people changes. 
//		ignoredFiles.addAll(svnFiles.keySet());
		HashMap<String, Date> workingCopy = getWorkingCopy(svnFiles);
		mapClassFqnToLastRevisionTimestamp.putAll(workingCopy);
		PullModel pullModel = new PullModel(LighthouseModel.getInstance());
		Collection<LighthouseEvent> events = pullModel.executeQueryCheckout(workingCopy);
//		LighthouseModel.getInstance().fireModelChanged();
		fireModificationsToUI(events);
		logger.info("Number of events fetched after checkout = " + events.size());
	}
	
	@Override
	public void update(Map<IFile, ISVNInfo> svnFiles) {
		// Ignore in the change event these files, once we don't want to generate deltas for other people changes. 
//		ignoredFiles.addAll(svnFiles.keySet());
		
		// setar a lista de arquivos que foram dados updates
		// o metodo change vai ser chamado, dai eu nao quero gerar delta
		HashMap<String, Date> workingCopy = getWorkingCopy(svnFiles);
		mapClassFqnToLastRevisionTimestamp.putAll(workingCopy);
		
		LighthouseModel model = LighthouseModel.getInstance();
		LighthouseModelManager modelManager = new LighthouseModelManager(model); 
		
		modelManager.removeArtifactsAndEventsInside(workingCopy.keySet());
		
		checkout(svnFiles);
		
		// I need to "re-paint" the relationships that point to this incoming class
		LighthouseModel.getInstance().fireModelChanged();
	}
	
	@Override
	public void commit(Map<IFile, ISVNInfo> svnFiles) {
		HashMap<String, Date> workingCopy = getWorkingCopy(svnFiles);
		mapClassFqnToLastRevisionTimestamp.putAll(workingCopy);
		try {
			PushModel pushModel = new PushModel(LighthouseModel.getInstance());

			// assuming that there is just one committed time
			ISVNInfo[] svnInfo = svnFiles.values().toArray(new ISVNInfo[0]);
			Date svnCommittedTime = svnInfo[0].getLastChangedDate();
			
			Collection<LighthouseEvent> listEvents = pushModel.updateCommittedEvents( 
														getClassesFullyQualifiedName(svnFiles), 
														svnCommittedTime,
														Activator.getDefault().getAuthor());

			LighthouseModelManager modelManager = new LighthouseModelManager(LighthouseModel.getInstance()); 
			modelManager.removeCommittedEvents(workingCopy.keySet(),svnCommittedTime);
			
			fireModificationsToUI(listEvents);
			logger.debug("commitTime[ "+ svnCommittedTime + " ]");

		} catch (Exception e) {
			logger.error(e);
		}
	}


	private HashMap<String, Date> getWorkingCopy(Map<IFile, ISVNInfo> svnFiles) {
		HashMap<String, Date> result = new HashMap<String, Date>();
		for (Entry<IFile, ISVNInfo> entry : svnFiles.entrySet()) {
			String fqn = getClassFullyQualifiedName(entry.getKey());
			if (fqn != null) {
				ISVNInfo svnInfo = entry.getValue();
				result.put(fqn, svnInfo.getLastChangedDate());
			}
		}
		return result;
	}

	private List<String> getClassesFullyQualifiedName(
			Map<IFile, ISVNInfo> svnFiles) {
		LinkedList<String> result = new LinkedList<String>();
		for (IFile iFile : svnFiles.keySet()) {
			String fqn = getClassFullyQualifiedName(iFile);
			if (fqn != null) {
				result.add(fqn);
			}
		}
		return result;
	}

	private String getClassFullyQualifiedName(IFile iFile) {
		String result = null;
		try {
			/*
			 * When the Java file is out of sync with eclipse, get the fully
			 * qualified name from ICompilationUnit doesn't work. So we decide
			 * to do this manually, reading the file from the file system and
			 * parsing it.
			 */
			BufferedReader d = new BufferedReader(new InputStreamReader(
					new FileInputStream(iFile.getLocation().toOSString())));
			while (d.ready()) {
				String line = d.readLine();
				if (line.contains("package")) {
					String[] tokens = line.split("package\\s+|;");
					for (String token : tokens) {
						if (token.matches("[\\w\\.]+")) {
							result = token;
							break;
						}
					}
					break;
				}
			}
			/*
			 * Java files should have at least one class with the same name of
			 * the file
			 */
			String fileNameWithoutExtension = iFile.getName().replaceAll(
					".java", "");
			result += "." + fileNameWithoutExtension;
		} catch (Exception e) {
			logger.error(e);
		}

		return result;
	}
	private void fireModificationsToUI(Collection<LighthouseEvent> events) {
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
			model.fireClassChanged(entry.getKey(), entry.getValue());
		}
		// Fire relationship changes to the UI
		for (Entry<LighthouseRelationship, TYPE> entry : mapRelationshipEvent
				.entrySet()) {
			model.fireRelationshipChanged(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void run() {
		threadRunning = true;
		while (threadRunning) {
			logger.debug("timeout[ " + lastDBAccess + " ]");
			// Sleep for the time defined by thread timeout
			try {
				try {
					refreshModelBasedOnLastDBAccess();
					Thread.sleep(threadTimeout);
				} catch (RuntimeException e) {
					threadSuspended = true;
					synchronized(this) {
	                    while (threadSuspended) {
	                        wait();
	                    }
	                }
				}
			} catch (InterruptedException e) {
				logger.error(e);
			}
		}
	};
	private synchronized Date getTimestamp(){
		return new Date();
	}

	@Override
	public void remove(IFile iFile, boolean hasErrors) {
		//FIXME: Gambis pra pegar FQN pelo caminho do arquivo. Melhorar depois usando o source folder do projeto
		String srcFolder = "/src/";
		String projectName = iFile.getProject().getName();
		int index = iFile.getFullPath().toOSString().indexOf(projectName);
		String classFqn = iFile.getFullPath().toOSString().substring(index+projectName.length()+srcFolder.length()).replaceAll("/", ".").replaceAll(".java", "");
		LighthouseFile lhBaseFile = getBaseVersionFromDB(classFqn);
		if (lhBaseFile != null) {

			LighthouseDelta delta = new LighthouseDelta(Activator.getDefault()
					.getAuthor(), lhBaseFile, null);

			PushModel pushModel = new PushModel(LighthouseModel.getInstance());
			try {
				pushModel.updateModelFromEvents(delta.getEvents());
			} catch (Exception e) {
				logger.error(e);
			}
			fireModificationsToUI(delta.getEvents());

		}

	}

	/** The method add happens when a class is added in the workspace or when is checkout a project in a empty workspace. */
	@Override
	public void add(IFile iFile, boolean hasErrors) {
		final String classFqn = getClassFullyQualifiedName(iFile);
		
		// It is a new class created by the user. We are assuming that the user creates a class that doesn't contain errors.
		if (!mapClassFqnToLastRevisionTimestamp.containsKey(classFqn)) {
			mapClassFqnToLastRevisionTimestamp.put(classFqn, new Date(0));
			Collection<LighthouseEvent> deltaEvents = generateDeltaFromBaseVersion(Collections.singleton(iFile),true);
			// TODO: Think about DB operations in a thread
			PushModel pushModel = new PushModel(
					LighthouseModel.getInstance());
			try {
				pushModel.updateModelFromEvents(deltaEvents);
			} catch (Exception e) {
				// TODO: Try to throw up this exception
				logger.error(e);
			}
			fireModificationsToUI(deltaEvents);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (threadSuspended){
			synchronized(this) {
				threadSuspended = false;
				notify();
			}
		}
		
	}



//	/** FIXME: delete this later. just for demo purpose */
//	private void loadMap() {
//		try {
//			SimpleDateFormat formatter = new SimpleDateFormat(
//					"yyyy-MM-dd HH:mm:ss");
//			Date committedDate = formatter.parse("2009-11-03 20:30:00");
//			mapClassFqnToLastRevisionTimestamp.put(
//					"edu.prenticehall.deitel.Account", committedDate);
//			mapClassFqnToLastRevisionTimestamp.put(
//					"edu.prenticehall.deitel.ATM", committedDate);
//			mapClassFqnToLastRevisionTimestamp.put(
//					"edu.prenticehall.deitel.ATMCaseStudy", committedDate);
//			mapClassFqnToLastRevisionTimestamp.put(
//					"edu.prenticehall.deitel.BalanceInquiry", committedDate);
//			mapClassFqnToLastRevisionTimestamp.put(
//					"edu.prenticehall.deitel.BankDatabase", committedDate);
//			mapClassFqnToLastRevisionTimestamp.put(
//					"edu.prenticehall.deitel.CashDispenser", committedDate);
//			mapClassFqnToLastRevisionTimestamp.put(
//					"edu.prenticehall.deitel.Deposit", committedDate);
//			mapClassFqnToLastRevisionTimestamp.put(
//					"edu.prenticehall.deitel.DepositSlot", committedDate);
//			mapClassFqnToLastRevisionTimestamp.put(
//					"edu.prenticehall.deitel.Keypad", committedDate);
//			mapClassFqnToLastRevisionTimestamp.put(
//					"edu.prenticehall.deitel.Screen", committedDate);
//			mapClassFqnToLastRevisionTimestamp.put(
//					"edu.prenticehall.deitel.Transaction", committedDate);
//			mapClassFqnToLastRevisionTimestamp.put(
//					"edu.prenticehall.deitel.Withdrawal", committedDate);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//	}

}
