package edu.uci.lighthouse.extensions.codereview.util;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.MatchResult;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.MarkerUtilities;

import edu.uci.lighthouse.extensions.codereview.Activator;
import edu.uci.lighthouse.extensions.codereview.model.CodeReview;
import edu.uci.lighthouse.extensions.codereview.model.CodeReviewModel;
import edu.uci.lighthouse.extensions.codereview.model.CodeSelection;
import edu.uci.lighthouse.extensions.codereview.model.FileSnapshot;
import edu.uci.lighthouse.extensions.codereview.ui.RemoteFileEditorInput;

public class EditorUtility {

	public static void openLocalFileInEditor(FileSnapshot fs,
			CodeSelection cs) {
		String packageName = getPackageName(fs.getContent());
		if (packageName != null) {
			String fqn = packageName + "." + fs.getFilename().replaceAll(".java", "");
			try {
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				IJavaModel javaModel = JavaCore.create(workspace.getRoot());
				IJavaProject[] projects = javaModel.getJavaProjects();
				for (int i = 0; i < projects.length; i++) {
					IType type = projects[i].findType(fqn);
					if (type != null) {
						IJavaElement target = (IJavaElement) type;
						IMarker selection = null;
						if (cs != null) {
							selection = getMarker(cs, target.getResource());
						}
						IEditorPart editorPart = JavaUI.openInEditor(target,
								true, false);
						if (selection != null) {
							IDE.gotoMarker(editorPart, selection);
						}
						break;
					}
				}
			} catch (Exception ex) {
				// logger.error(ex,ex);
			}
		}
	}
	
	public static void openRemoteFileInEditor(FileSnapshot fs, CodeSelection cs) {
		String packageName = getPackageName(fs.getContent());
		if (packageName != null) {
			try {
				CodeReview review = CodeReviewModel.getInstance().getReview(fs);
				IWorkspace ws = ResourcesPlugin.getWorkspace();
				IProject project = ws.getRoot().getProject("."+Activator.PLUGIN_ID+"."+review.getId());
				if (!project.exists()) {
					project.create(null);
				}
				if (!project.isOpen()) {
					project.open(null);
				}
				project.setHidden(true);
				
				IFolder folder = project.getFolder("src");
				if (!folder.exists()) {
					folder.create(true, true, null);
				}
				IJavaProject jProject = JavaCore.create(project);
				IPackageFragmentRoot srcFolder = jProject
						.getPackageFragmentRoot(folder);
				srcFolder.createPackageFragment(packageName, true, null);

				IFile iFile = project.getFile(new Path("src/"
						+ packageName.replaceAll("\\.", "/") + "/"
						+ fs.getFilename()));
				if (!iFile.exists()) {
					iFile.create(new ByteArrayInputStream(fs.getContent()
							.getBytes()), true, null);
					ResourceAttributes ra = new ResourceAttributes();
					ra.setReadOnly(true);
					iFile.setResourceAttributes(ra);
				} else {
					ResourceAttributes ra = new ResourceAttributes();
					ra.setReadOnly(false);
					iFile.setResourceAttributes(ra);
					iFile.setContents(new ByteArrayInputStream(fs.getContent()
							.getBytes()), true, false, null);
					ra.setReadOnly(true);
					iFile.setResourceAttributes(ra);
				}
				
				syncMarkers(fs, iFile);
				
				IMarker selection = null;
				if (cs != null) {
					selection = getMarker(cs, iFile);
				}

				IWorkbenchWindow window = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow();
				IWorkbenchPage page = window.getActivePage();

				IEditorPart editorPart = IDE.openEditor(page, new RemoteFileEditorInput(iFile, fs), IDE
						.getEditorDescriptor(fs.getFilename()).getId());
//				IEditorPart editorPart = IDE.openEditor(page, new FileEditorInput(iFile), IDE
//				.getEditorDescriptor(fs.getFilename()).getId());
				if (selection != null) {
					IDE.gotoMarker(editorPart, selection);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void removeAllLocalMarkers(CodeReview review) {
		for (FileSnapshot fs: review.getFilesSnapshot()){
		String packageName = getPackageName(fs.getContent());
		if (packageName != null) {
			String fqn = packageName + "." + fs.getFilename().replaceAll(".java", "");
			try {
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				IJavaModel javaModel = JavaCore.create(workspace.getRoot());
				IJavaProject[] projects = javaModel.getJavaProjects();
				for (int i = 0; i < projects.length; i++) {
					IType type = projects[i].findType(fqn);
					if (type != null) {
						for(CodeSelection cs: fs.getCodeSelection()) {
							IMarker marker = getMarker(cs, type.getResource());
							if (marker != null) {
								marker.delete();
							}
						}
						break;
					}
				}
			} catch (Exception ex) {
				// logger.error(ex,ex);
			}
		}
		}
	}
	
	public static void syncMarkers(FileSnapshot fs, IResource iResource){
			for (CodeSelection cs: fs.getCodeSelection()){
				IMarker marker = getMarker(cs, iResource);
				if (marker == null) {
					createMarker(cs, iResource);
				}
			}
	}
	
	public static void createMarker(CodeSelection cs, IResource iResource) {
		int charStart = cs.getSelection().getOffset();
		int charEnd = charStart + cs.getSelection().getLength();
		createMarker(charStart, charEnd, iResource);
	}		
	
	public static void createMarker(int charStart, int charEnd, IResource iResource) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		MarkerUtilities.setCharStart(map, charStart);
		MarkerUtilities.setCharEnd(map, charEnd);
		try {
			MarkerUtilities.createMarker(iResource, map, "LighthouseCodeReview.requestMarker");
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}	
	

	private static IMarker getMarker(CodeSelection cs, IResource iFile) {
		IMarker result = null;
		try {
			IMarker[] markers = iFile.findMarkers(
					"LighthouseCodeReview.requestMarker", true,
					IResource.DEPTH_INFINITE);
			int charStart = cs.getSelection().getOffset();
			int charEnd = charStart + cs.getSelection().getLength();
			for (IMarker m : markers) {
				if (charStart == MarkerUtilities.getCharStart(m)
						&& charEnd == MarkerUtilities.getCharEnd(m)) {
					result = m;
					break;
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static String getPackageName(String fileContent) {
		String result = null;
		Scanner s = new Scanner(fileContent);
		s.findInLine("package ((\\w+\\.*)+);");
		 MatchResult match = s.match();
		 if (match.groupCount() > 0) {
			 result = match.group(1);
		 }
		 s.close();
		return result;
	}
	
}
