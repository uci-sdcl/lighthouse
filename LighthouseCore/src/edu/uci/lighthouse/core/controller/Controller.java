/*******************************************************************************
* Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
*				, University of California, Irvine}.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    {Software Design and Collaboration Laboratory (SDCL)
*	, University of California, Irvine}
*			- initial API and implementation and/or initial documentation
*******************************************************************************/
package edu.uci.lighthouse.core.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.BundleContext;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNInfo;
import org.tigris.subversion.svnclientadapter.ISVNStatus;
import org.tigris.subversion.svnclientadapter.SVNStatusKind;

import edu.uci.lighthouse.core.data.DatabaseActionsThread;
import edu.uci.lighthouse.core.data.ISubscriber;
import edu.uci.lighthouse.core.data.PersistableRegistry;
import edu.uci.lighthouse.core.dbactions.CompoundAction;
import edu.uci.lighthouse.core.dbactions.CreateNewDatabaseConnectionAction;
import edu.uci.lighthouse.core.dbactions.DatabaseActionsBuffer;
import edu.uci.lighthouse.core.dbactions.IDatabaseAction;
import edu.uci.lighthouse.core.dbactions.JobDecoratorAction;
import edu.uci.lighthouse.core.dbactions.SaveUserAction;
import edu.uci.lighthouse.core.dbactions.pull.CheckoutAction;
import edu.uci.lighthouse.core.dbactions.pull.SynchronizeModelAction;
import edu.uci.lighthouse.core.dbactions.pull.UpdateAction;
import edu.uci.lighthouse.core.dbactions.push.CommitAction;
import edu.uci.lighthouse.core.dbactions.push.FileEventAction;
import edu.uci.lighthouse.core.listeners.IJavaFileStatusListener;
import edu.uci.lighthouse.core.listeners.ILHclassPluginExtensionObserver;
import edu.uci.lighthouse.core.listeners.IPluginListener;
import edu.uci.lighthouse.core.listeners.ISVNEventListener;
import edu.uci.lighthouse.core.parser.LighthouseParser;
import edu.uci.lighthouse.core.preferences.DatabasePreferences;
import edu.uci.lighthouse.core.preferences.IPreferencesChangeListener;
import edu.uci.lighthouse.core.preferences.PreferencesNotifier;
import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.core.util.WorkbenchUtility;

import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseFile;
import edu.uci.lighthouse.model.LighthouseFileManager;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.parser.ParserException;

public class Controller implements ISVNEventListener, IJavaFileStatusListener,
IPluginListener, IPreferencesChangeListener /*, Runnable, IPropertyChangeListener*/ {

	private static Logger logger = Logger.getLogger(Controller.class);
	/*
	 * After the file get the changes from the repository, the event 'change'
	 * will be throw and we can wrong interpret the new changes being
	 * modifications made by the user. We need this list to ignore those files.
	 */
	private Collection<IFile> ignorefilesJustUpdated = new LinkedHashSet<IFile>();
	private DatabaseActionsBuffer buffer;
	private LighthouseModel model;
	private DatabaseActionsThread thread;
	
	
	/**@author lee*/
	private static Controller controller;
	/**@author lee*/
	ArrayList<ISubscriber> subscribers = new ArrayList<ISubscriber>();
	
	private Controller(){}
	
	/**
	 * @author lee
	 * @return
	 */
	public static Controller getInstance(){
		if(controller == null){
			controller = new Controller();
		}
		return controller;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		PreferencesNotifier.getInstance().addPreferencesChangeListener(this);
		
		loadPersistableResources();
		if (!model.isEmpty()) {
			LighthouseModel.getInstance().fireModelChanged();
		} 
		thread = new DatabaseActionsThread(buffer);
		thread.start(context);
		WorkbenchUtility.updateProjectIcon();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		PreferencesNotifier.getInstance().removePreferencesChangeListener(this);
		thread.stop(context);
		savePersistableResources();
	}
	
	private void loadPersistableResources() {
		logger.info("Loading persistable resources...");
		buffer = (DatabaseActionsBuffer) PersistableRegistry.getInstance(DatabaseActionsBuffer.class);
		model = (LighthouseModel) PersistableRegistry.getInstance(LighthouseModel.class);
	}
	
	private void savePersistableResources() {
		PersistableRegistry.saveInstances();
	}
	
	@Override
	public void open(final IFile iFile, boolean hasErrors) {
		// DO NOTHING
	}

	@Override
	public void close(IFile iFile, boolean hasErrors) {
		// DO NOTHING
	}

	@Override
	public void change(final IFile iFile, boolean hasErrors) {
		if (ModelUtility.belongsToImportedProjects(iFile, false)) {
			if (ignorefilesJustUpdated.contains(iFile)) {
				ignorefilesJustUpdated.remove(iFile);
			} else {
				try {
					IFile previousIFile = getPreviousVersion(iFile);
					if (previousIFile != null) {
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
					ModelUtility.getAuthor(),
					previousLhFile,
					currentLhFile);

			LinkedHashSet<LighthouseEvent> deltaEvents = delta.getEvents();
 
			if (deltaEvents.size() == 0) {
				logger.error("We have an workspace event without delta elements (delta==0)");
			} else {
				logger.debug("Workspace event generated delta events: "+ deltaEvents.size());
			}
			
			UpdateLighthouseModel.addEvents(deltaEvents);
			ModelUtility.fireModificationsToUI(deltaEvents);
			
			FileEventAction fileEventAction = new FileEventAction(deltaEvents);
			buffer.offer(fileEventAction);
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
	 * The method add run when a class is added in the workspace or when
	 * the user checkout a project in a empty workspace.
	 */
	@Override
	public void add(IFile iFile, boolean hasErrors) {
		if (ModelUtility.belongsToImportedProjects(iFile, false)) {
			if (!existsInSVN(iFile)) {
				try {
					// Created files has history.
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
			
			try {
				IFile previousIFile = getPreviousVersion(iFile);
				if (previousIFile != null) {
					generateDeltaAndSaveIntoModel(previousIFile, null);
					removeIFile(previousIFile);
				} else { // there is no history for this file
					// FIXME Tiago is trying to get the remove event before actually remove the file
				}
			} catch (Exception e) {
				logger.error(e, e);
			}
		}
	}

	
	@Override
	public void checkout(Map<IFile, ISVNInfo> svnFiles) {
		CheckoutAction checkoutAction = new CheckoutAction(svnFiles);
		buffer.offer(checkoutAction);
	}

	@Override
	public void update(Map<IFile, ISVNInfo> svnFiles) {
		// Ignore in this files in the change() event, because we don't want to
		// generate deltas for other people changes.
		ignorefilesJustUpdated.addAll(svnFiles.keySet());
		
		UpdateAction updateAction = new UpdateAction(svnFiles);
		buffer.offer(updateAction);
	}

	@Override
	public void commit(Map<IFile, ISVNInfo> svnFiles) {		
		// Get events for committing from SVN files.
		Collection<LighthouseEvent> eventsforCommitting = ModelUtility
				.getEventsForCommiting(svnFiles);

		// Remove committed events and artifacts from the model.
		LighthouseModelManager manager = new LighthouseModelManager(model);
		manager.removeCommittedEventsAndArtifacts(ModelUtility.getClassesFullyQualifiedName(svnFiles));

		// Refresh the UI.
		ModelUtility.fireModificationsToUI(eventsforCommitting);
		
		CommitAction commitAction = new CommitAction(eventsforCommitting);
		buffer.offer(commitAction);
	}

	@Override
	public void conflict(Map<IFile, ISVNInfo> svnFiles) {
		// DO NOTHING
	}

	public IFile getPreviousVersion(IFile from) throws Exception{
		String fromFullPath = from.getFullPath().toPortableString();
		String fromFilename = from.getFullPath().toFile().getName();
		String iFileFullPath = fromFullPath.replaceAll(fromFilename, "TEMP_RESOURCE_LHPreviousFile.java");
		// This file name should be VERY unique otherwise we will have a file name conflict

		IFileState[] history = from.getHistory(null);
		if (history.length==0) {
			return null;
		}
		
		InputStream inputStream = history[0].getContents();

		IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(iFileFullPath));

		IProject project = from.getProject();
		project.getFile(iFileFullPath);
		//IFile iFile = from.getProject().getFile(iFileFullPath);
		iFile.create(inputStream, true, null);
		
		return iFile;
	}
	
	private boolean existsInSVN(IFile iFile) {
		boolean result = false;
		try {
			ISVNClientAdapter svnAdapter = SVNProviderPlugin.getPlugin()
			.getSVNClient();
			 ISVNStatus status = svnAdapter.getSingleStatus(iFile.getLocation().toFile());
			 result = status.getTextStatus() != SVNStatusKind.UNVERSIONED;
		} catch (Exception e) {
			logger.error(e, e);
		}
		return result;
	}

	private void removeIFile(IFile iFile) throws CoreException {
		iFile.delete(true, null);
	}

	@Override
	public void userChanged() {
		buffer.offer(new SaveUserAction());
	}

	@Override
	public void dbSettingsChanged() {
		thread.pause();
		buffer.clear();
		CompoundAction compoundAction = new CompoundAction();
		compoundAction.add(new CreateNewDatabaseConnectionAction(DatabasePreferences.getDatabaseSettings()));
		compoundAction.add(new SynchronizeModelAction(WorkbenchUtility.getSVNInfoFromWorkspace()));
		buffer.offer(new JobDecoratorAction(compoundAction, "Synchronize Model","Synchronizing model with database..."));
		thread.play();
	}
	
	/**
	 * Used by other plugins to add DataBaseActions to the buffer
	 * @author lee
	 * @return
	 */
	public DatabaseActionsBuffer getBuffer(){
		return buffer;
	}
	
	
	/**
	 * @author lee
	 * @param subscriber
	 */
	public void subscribeToLighthouseEvents(ISubscriber subscriber){
		this.subscribers.add(subscriber);
	}

	/**
	 * @author lee
	 * @return
	 */
	public List<ISubscriber> getSubscribers() {
		return subscribers;
	}
	



}
