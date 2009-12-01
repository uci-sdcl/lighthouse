package edu.uci.lighthouse.core.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.preference.IPreferenceStore;
import org.osgi.framework.BundleContext;
import org.tigris.subversion.svnclientadapter.ISVNInfo;

import edu.uci.lighthouse.core.Activator;
import edu.uci.lighthouse.core.listeners.IJavaFileStatusListener;
import edu.uci.lighthouse.core.listeners.IPluginListener;
import edu.uci.lighthouse.core.listeners.ISVNEventListener;
import edu.uci.lighthouse.core.parser.IParserAction;
import edu.uci.lighthouse.core.parser.LighthouseParser;
import edu.uci.lighthouse.model.BuildLHBaseFile;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseFile;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.LighthouseEvent.TYPE;

public class Controller implements ISVNEventListener, IJavaFileStatusListener,
		IPluginListener, Runnable {

	private static Logger logger = Logger.getLogger(Controller.class);
	private HashMap<String, Date> mapClassFqnToLastRevisionTimestamp = new HashMap<String, Date>();
	private HashMap<String, LighthouseFile> classBaseVersion = new HashMap<String, LighthouseFile>();
	private List<String> classWithErrors = new LinkedList<String>();
	private Date lastDBAccess = null;
	private boolean threadRunning;
	private final int threadTimeout = 5000;

	@Override
	public void start(BundleContext context) throws Exception {
		loadPreferences();
		loadMap();
		loadModel();
		//(new Thread(this)).start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		threadRunning = false;
		savePreferences();
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
	 * Refresh the LighthouseModel with new events from database and fire this changes to the UI.
	 */
	public synchronized void refreshModelBasedOnLastDBAccess() {
		/*
		 * If the map's size == 0, it means that it is the first time that user
		 * is running Lighthouse. Then, the LighthouseModel will be updated only
		 * if the user execute a checkout first.
		 */
		if (mapClassFqnToLastRevisionTimestamp.size() != 0) {
			PullModel pullModel = new PullModel(LighthouseModel.getInstance());
			List<LighthouseEvent> events = pullModel.getNewEventsFromDB(lastDBAccess);
			fireModificationsToUI(events);
			lastDBAccess = getTimestamp();
		}
	}

	private void loadModel() {
		PullModel pullModel = new PullModel(LighthouseModel.getInstance());
		Collection<LighthouseEvent> events = pullModel.executeQueryCheckout(mapClassFqnToLastRevisionTimestamp);
		fireModificationsToUI(events);		
	}

	@Override
	public void open(final IFile iFile, boolean hasErrors) {
		final String classFqn = getClassFullyQualifiedName(iFile);
		logger.debug("open "+classFqn);
		Date revisionTime = mapClassFqnToLastRevisionTimestamp.get(classFqn);
		LighthouseFile lhBaseFile = BuildLHBaseFile.execute(LighthouseModel.getInstance(), classFqn, revisionTime, Activator.getDefault().getAuthor());
		classBaseVersion.put(classFqn,lhBaseFile);
	}
	
	@Override
	public void change(final IFile iFile, boolean hasErrors) {
		final String classFqn = getClassFullyQualifiedName(iFile);
		if (hasErrors) {
			classWithErrors.add(classFqn);
		} else {
			// verify if any of the classWithErrors still have errors
			// than parse the files that does not have errors anymore
			// and remove the classes from the list classWithErrors
			try {
				final LighthouseFile currentLhFile = new LighthouseFile();
				LighthouseParser parser = new LighthouseParser();
				parser.executeInAJob(currentLhFile, Collections
						.singleton(iFile), new IParserAction() {
					@Override
					public void doAction() {
						LighthouseFile lhBaseFile = classBaseVersion.get(classFqn);
						if (lhBaseFile==null) {
							// raise exception there is something wrong here
						}
						LighthouseDelta delta = new LighthouseDelta(Activator
								.getDefault().getAuthor(), lhBaseFile,
								currentLhFile);
						PushModel pushModel = new PushModel(LighthouseModel
								.getInstance());
						try {
							pushModel.updateModelFromDelta(delta);
						} catch (Exception e) {
							// TODO: Try to throw up this exception
							logger.error(e);
						}
						// Updates the current base version
						classBaseVersion.put(classFqn, currentLhFile);
						fireModificationsToUI(delta.getEvents());
					}
				});
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	@Override
	public void close(IFile iFile, boolean hasErrors) {
		final String classFqn = getClassFullyQualifiedName(iFile);
		if (!hasErrors) {
			classBaseVersion.remove(classFqn);
		}
	}
	
	@Override
	public void removed(IFile iFile, boolean hasErrors) {
	}
	
	@Override
	public void checkout(Map<IFile, ISVNInfo> svnFiles) {
		HashMap<String, Date> workingCopy = getWorkingCopy(svnFiles);
		PullModel pullModel = new PullModel(LighthouseModel.getInstance());
		Collection<LighthouseEvent> events = pullModel.executeQueryCheckout(workingCopy);
		fireModificationsToUI(events);
	}

	@Override
	public void update(Map<IFile, ISVNInfo> svnFiles) {
		// setar a lista de arquivos que foram dados updates
		// o metodo change vai ser chamado, dai eu nao quero gerar delta
		HashMap<String, Date> workingCopy = getWorkingCopy(svnFiles);
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
			Date svnCommittedTime = getTimestamp();
			Collection<LighthouseEvent> listEvents = pushModel.updateCommittedEvents( 
														getClassesFullyQualifiedName(svnFiles), 
														svnCommittedTime,
														Activator.getDefault().getAuthor());
			fireModificationsToUI(listEvents);
			logger.debug("commitTime[ "+ svnCommittedTime + " ]");
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private HashMap<String, Date> getWorkingCopy(
			Map<IFile, ISVNInfo> svnFiles) {
		HashMap<String, Date> result = new HashMap<String, Date>();
		for (Entry<IFile, ISVNInfo> entry : svnFiles.entrySet()) {
			String fqn = getClassFullyQualifiedName(entry.getKey());
			if (fqn != null) {
				ISVNInfo svnInfo = entry.getValue();
				result.put(fqn, svnInfo
						.getLastChangedDate());
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
			 * qualified name from ICompilationUnit doesn't work. So we decide to do
			 * this manually, reading the file from the file system and parsing it.
			 */
			BufferedReader d = new BufferedReader(new InputStreamReader(new FileInputStream(iFile.getLocation().toOSString())));
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
			result += "."+fileNameWithoutExtension;			
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
			//TODO: comment more this method. It is confusing.
			// ADD creates a new class node in the view and populates it
			// MODIFY just re-populates the class node
			if (artifact instanceof LighthouseClass) {
				LighthouseClass klass = (LighthouseClass) artifact;
				mapClassEvent.put(klass, event.getType());
			} else if (artifact instanceof LighthouseEntity) {
				LighthouseModelManager manager = new LighthouseModelManager(model);
				LighthouseClass klass = manager.getMyClass((LighthouseEntity) artifact);
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
			model.fireClassChanged(entry.getKey(),entry.getValue());
		}
		// Fire relationship changes to the UI
		for (Entry<LighthouseRelationship, TYPE> entry : mapRelationshipEvent.entrySet()) {
			model.fireRelationshipChanged(entry.getKey(),entry.getValue());
		}
	}

	@Override
	public void run() {
		threadRunning = true;
		while (threadRunning) {
			logger.debug("timeout[ "+ lastDBAccess + " ]");
			refreshModelBasedOnLastDBAccess();
			// Sleep for the time defined by thread timeout
			try {
				Thread.sleep(threadTimeout);
			} catch (InterruptedException e) {
				logger.error(e);
			}
		}
	};
	
	private synchronized Date getTimestamp(){
		return new Date();
	}

	/** FIXME: delete this later. just for demo purpose*/
	private void loadMap() {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date committedDate = formatter.parse("2009-11-03 20:30:00");
			mapClassFqnToLastRevisionTimestamp.put("edu.prenticehall.deitel.Account", committedDate);
			mapClassFqnToLastRevisionTimestamp.put("edu.prenticehall.deitel.ATM", committedDate);
			mapClassFqnToLastRevisionTimestamp.put("edu.prenticehall.deitel.ATMCaseStudy", committedDate);
			mapClassFqnToLastRevisionTimestamp.put("edu.prenticehall.deitel.BalanceInquiry", committedDate);
			mapClassFqnToLastRevisionTimestamp.put("edu.prenticehall.deitel.BankDatabase", committedDate);
			mapClassFqnToLastRevisionTimestamp.put("edu.prenticehall.deitel.CashDispenser", committedDate);
			mapClassFqnToLastRevisionTimestamp.put("edu.prenticehall.deitel.Deposit", committedDate);
			mapClassFqnToLastRevisionTimestamp.put("edu.prenticehall.deitel.DepositSlot", committedDate);			
			mapClassFqnToLastRevisionTimestamp.put("edu.prenticehall.deitel.Keypad", committedDate);
			mapClassFqnToLastRevisionTimestamp.put("edu.prenticehall.deitel.Screen", committedDate);
			mapClassFqnToLastRevisionTimestamp.put("edu.prenticehall.deitel.Transaction", committedDate);
			mapClassFqnToLastRevisionTimestamp.put("edu.prenticehall.deitel.Withdrawal", committedDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
		
}
