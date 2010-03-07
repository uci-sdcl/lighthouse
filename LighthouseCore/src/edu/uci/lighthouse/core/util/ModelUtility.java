package edu.uci.lighthouse.core.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;

import edu.uci.lighthouse.model.LighthouseModel;

public class ModelUtility {
	public static boolean belongsToImportedProjects(IFile iFile) {
		IJavaElement jFile = JavaCore.create(iFile);
		if (jFile != null) {
			String projectName = jFile.getJavaProject().getElementName();
			LighthouseModel model = LighthouseModel.getInstance();
			if (model.getProjectNames().contains(projectName)) {
				return true;
			}
		}
		return false;
	}
}
