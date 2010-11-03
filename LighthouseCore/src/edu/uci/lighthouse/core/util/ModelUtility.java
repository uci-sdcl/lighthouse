package edu.uci.lighthouse.core.util;

import java.io.File;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.tigris.subversion.svnclientadapter.ISVNInfo;

import edu.uci.lighthouse.core.preferences.DatabasePreferences;
import edu.uci.lighthouse.core.preferences.UserPreferences;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseEvent.TYPE;
import edu.uci.lighthouse.model.LighthouseInterface;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseModelUtil;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.jpa.LHEntityDAO;
import edu.uci.lighthouse.model.util.DatabaseUtility;
import edu.uci.lighthouse.model.util.LHStringUtil;

/**
 * This is a utility class that helps extract information and navigate through the lighthouse model
 */
public class ModelUtility {

	private static Logger logger = Logger.getLogger(ModelUtility.class);
	
	private static LighthouseAuthor author;
	
	/**
	 * Verify if some given iFile belongs to the Projects that was imported to the database
	 * 
	 * @param iFile
	 * @param checkDatabase
	 * @return
	 */
	public static boolean belongsToImportedProjects(IFile iFile, boolean checkDatabase) {
		IJavaElement jFile = JavaCore.create(iFile);
		if (jFile != null) {
			/* When the user is checking out, there is no working copy to compare with. So, we have to compare with the database to guarantee that imported projects existing in database can be checkout. */
			if (checkDatabase) {
				String clazzName = getClassFullyQualifiedName(iFile);
				try {
					LighthouseEntity entity = new LHEntityDAO().get(LHStringUtil.getMD5Hash(clazzName));
					return (entity!=null);
				} catch (Exception e) {
					logger.info("Class: " + clazzName + " is not on the database");
				}
			}
			String projectName = jFile.getJavaProject().getElementName();
			LighthouseModel model = LighthouseModel.getInstance();
			if (model.getProjectNames().contains(projectName)) {
				return true;
			}
		}
		return false;
	}

	public static List<String> getClassesFullyQualifiedName(
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

	public static String getClassFullyQualifiedName(IFile iFile) { 
		String result = "";
		try {
			IJavaProject jProject = (IJavaProject) iFile.getProject().getNature(JavaCore.NATURE_ID);
			String[] sourceFolders = WorkbenchUtility.getSourceFolders(jProject);
			for (String srcFolder : sourceFolders) {
				String fullPath = iFile.getFullPath().toOSString();
				if (fullPath.indexOf(srcFolder) != -1) {
					String projectName = iFile.getProject().getName();
					int index = fullPath.indexOf(srcFolder) + srcFolder.length() + 1;
					String classFqn = fullPath.substring(index).replaceAll(File.separator, ".").replaceAll(".java", "");
					result = projectName + "." + classFqn;
					break;
				}
			}
		} catch (CoreException e) {
			logger.error(e,e);
		}
		return result;
	}
	
	/**
	 * Verifies if the class defined by the given fqn exists in the workspace.
	 */
	public static boolean existsInWorkspace(String fqn) {
		int index = fqn.indexOf(".");
		if (index != -1) {
			String projectName = fqn.substring(0,index);
			String relativePath = File.separator + fqn.substring(index + 1).replaceAll("\\.", File.separator) + ".java";
			
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IProject[] projects = workspace.getRoot().getProjects();
			for (IProject project : projects) {
				if (project.isOpen() && projectName.equals(project.getName())) {
					try {
						IJavaProject jProject = (IJavaProject) project
						.getNature(JavaCore.NATURE_ID);
						String[] sourceFolders = WorkbenchUtility.getSourceFolders(jProject);
						for (String srcFolder : sourceFolders) {
							String fileName = project.getLocation().toOSString().replaceAll(File.separator+projectName, "") + srcFolder + relativePath;
							File file = new File(fileName);
							if (file.exists()) {
								return true;
							}
						}
					} catch (CoreException e) {
						logger.error(e, e);
					}
				}
			}
		}
		return false;		
	}
	
	/**
	 * Get the input list of events and update the UI accordingly 
	 * @param events
	 */
	public static void fireModificationsToUI(Collection<LighthouseEvent> events) {
		logger.debug("fireModificationsToUI ("+events.size()+" events)");
		// We use a Hashmap in order to avoid repaint the UI multiple times
		HashMap<LighthouseEntity, LighthouseEvent.TYPE> mapClassEvent = new HashMap<LighthouseEntity, LighthouseEvent.TYPE>();
		HashMap<LighthouseRelationship, LighthouseEvent.TYPE> mapRelationshipEvent = new HashMap<LighthouseRelationship, LighthouseEvent.TYPE>();
		LighthouseModel model = LighthouseModel.getInstance();
		for (LighthouseEvent event : events) {
			// ADD creates a new class node in the view and populates it
			// MODIFY just re-populates the class node
			Object artifact = event.getArtifact();
			if (artifact instanceof LighthouseClass || artifact instanceof LighthouseInterface) {
				//LighthouseClass klass = (LighthouseClass) artifact;
				mapClassEvent.put((LighthouseEntity)artifact, event.getType());
			} else if (artifact instanceof LighthouseEntity) {
				LighthouseModelManager manager = new LighthouseModelManager(
						model);
				LighthouseEntity klass = manager
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
		for (Entry<LighthouseEntity, TYPE> entry : mapClassEvent.entrySet()) {
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
	
	/**
	 * This method figure out which event needs to be committed
	 * 
	 * @param svnFiles
	 * @return
	 */
	public static Collection<LighthouseEvent> getEventsForCommiting(Map<IFile, ISVNInfo> svnFiles/*List<String> listClazzFqn, Date svnCommittedTime, LighthouseAuthor author*/) {
		// The commit time is equal for all the files in the map. So, let's pick the first one.
		Date svnCommittedTime = svnFiles.values().toArray(new ISVNInfo[0])[0].getLastChangedDate();
		LighthouseAuthor author = getAuthor();
		Collection<LighthouseEvent> listEvents = LighthouseModelUtil.getEventsInside(LighthouseModel.getInstance(), getClassesFullyQualifiedName(svnFiles)); 
		LinkedHashSet<LighthouseEvent> listEventsToCommitt = new LinkedHashSet<LighthouseEvent>();
		for (LighthouseEvent event : listEvents) {
			if (event.getAuthor().equals(author)) {
				if (!event.isCommitted()) { // I am not sure whether we need this IF or not
					event.setCommitted(true);
					event.setCommittedTime(svnCommittedTime);
					listEventsToCommitt.add(event);
				}
			}
		}
		return listEventsToCommitt;
	}
	
	/**
	 * Adjust the committed time from all events using the database timezone.
	 * 
	 * @param listEvents
	 * @throws SQLException
	 */
	public static void adjustCommittedTimeToServerTime(Collection<LighthouseEvent> listEvents) throws SQLException {
		for (LighthouseEvent event : listEvents) {
			if (event.isCommitted()) {
				Date committedTime = event.getCommittedTime();
				Date adjustedCommittedTime = DatabaseUtility.getAdjustedDateTime(committedTime, DatabasePreferences.getServerTimezone());
				event.setCommittedTime(adjustedCommittedTime);
			}
		}
	}
	
	public static LighthouseAuthor getAuthor() {
		if (author == null){
			Properties userSettings = UserPreferences.getUserSettings();
			String userName = userSettings.getProperty(UserPreferences.USERNAME);
			author = new LighthouseAuthor(userName);
		}
		return author;
	}
	
	public static boolean hasImportedProjects(IWorkspace workspace) {
		LighthouseModel model = LighthouseModel.getInstance();
		IProject[] projects = workspace.getRoot().getProjects();
		for (IProject project : projects) {
			if (project.isOpen()) {
				if (model.getProjectNames().contains(project.getName())) {
					return true;
				}
			}
		}
		return false;
	}

}
