package edu.uci.lighthouse.test.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;

import edu.uci.lighthouse.core.parser.LighthouseParser;
import edu.uci.lighthouse.model.LighthouseAbstractModel;
import edu.uci.lighthouse.model.LighthouseFile;

public class UtilModel {

	public static void createModel(final IWorkspace workspace, List<String> listProjects, LighthouseAbstractModel model) {
		IProject[] projects = workspace.getRoot().getProjects();
		final Collection<IFile> files = new LinkedList<IFile>();
		for (int i = 0; i < projects.length; i++) {
			if (listProjects.contains(projects[i].getName())) {
				if (projects[i].isOpen()) {
					files.addAll(getFilesFromProject(projects[i]));
				}
			}
		}
		if (files.size() > 0) {
			LighthouseParser parser = new LighthouseParser();
			parser.execute(model, files);
		}
	}

	private static Collection<IFile> getFilesFromProject(IProject project) {
		final Collection<IFile> files = new HashSet<IFile>();
		try {
			project.getFolder("src").accept(new IResourceVisitor() {
				@Override
				public boolean visit(IResource resource) throws CoreException {
					if (resource.getType() == IResource.FILE
							&& resource.getFileExtension().equalsIgnoreCase(
									"java")) {
						files.add((IFile) resource);
						return false;
					} else {
						return true;
					}
				}
			});
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return files;
	}

	public static LighthouseFile parseJustOneFile(final IWorkspace workspace, List<String> listProjects, String fileName) {
		IProject[] projects = workspace.getRoot().getProjects();
		for (int i = 0; i < projects.length; i++) {
			if (listProjects.contains(projects[i].getName())) {
				if (projects[i].isOpen()) {
					Collection<IFile> files = getFilesFromProject(projects[i]);
					for (IFile file : files) {
						if (file.getName().equals(fileName)) {
							LighthouseParser parser = new LighthouseParser();
							LighthouseFile lighthouseFile = new LighthouseFile();
							parser.execute(lighthouseFile, file);
							return lighthouseFile;
						}
					}
				}
			}
		} // end-of-for
		return null;
	}
	
}
