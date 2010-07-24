package edu.uci.lighthouse.core.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.tigris.subversion.svnclientadapter.ISVNInfo;

import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.jpa.LHEntityDAO;
import edu.uci.lighthouse.model.util.LHStringUtil;

public class ModelUtility {

	private static Logger logger = Logger.getLogger(ModelUtility.class);
	
	public static boolean belongsToImportedProjects(IFile iFile, boolean checkDatabase) {
		IJavaElement jFile = JavaCore.create(iFile);
		if (jFile != null) {
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
			/*
			 * When the Java file is out of sync with eclipse, get the fully
			 * qualified name from ICompilationUnit doesn't work. So we decide
			 * to do this manually, reading the file from the file system and
			 * parsing it.
			 */
			String packageName = null;
			BufferedReader d = new BufferedReader(new InputStreamReader(
					new FileInputStream(iFile.getLocation().toOSString())));
			while (d.ready()) {
				String line = d.readLine();
				if (line.contains("package")) {
					String[] tokens = line.split("package\\s+|;");
					for (String token : tokens) {
						if (token.matches("[\\w\\.]+")) {
							packageName = token;
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
			if (packageName == null) {
				result = iFile.getProject().getName() + "." + fileNameWithoutExtension;
			} else {
				result = iFile.getProject().getName() + "." + packageName + "." + fileNameWithoutExtension;
			}
		} catch (Exception e) {
			logger.error(e, e);
		}

		return result;
	}

}
