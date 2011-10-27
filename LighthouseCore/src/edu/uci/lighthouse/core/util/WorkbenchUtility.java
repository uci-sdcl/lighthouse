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
package edu.uci.lighthouse.core.util;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNInfo;
import org.tigris.subversion.svnclientadapter.ISVNStatus;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNStatusKind;

import edu.uci.lighthouse.core.decorators.LighthouseProjectLabelDecorator;

public class WorkbenchUtility {
	
	private static Logger logger = Logger.getLogger(WorkbenchUtility.class);

	public static IEditorPart getActiveEditor(){
		class UITask implements Runnable {
			IEditorPart activeEditor;
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow();
				IWorkbenchPage[] pages = window.getPages();
				for (IWorkbenchPage page : pages) {
					activeEditor = page.getActiveEditor();
				}
			}
		}
		UITask task = new UITask();
		Display.getDefault().syncExec(task);
		return task.activeEditor;
	}
	
	@Deprecated
	public static IStatusLineManager getStatusLineManager(){
		IEditorPart editor = getActiveEditor();
		if (editor != null){
			return editor.getEditorSite().getActionBars().getStatusLineManager();
		}
		return null;
	}
	
	public static void openPreferences(){
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				Shell shell = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell();
				String rootId = "edu.uci.lighthouse.core.preferences";
				PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(shell, rootId, new String[]{rootId+".database",rootId+".user"}, null);
				dialog.open();
			}
		});
	}
	
	public static void updateProjectIcon() {
		Display.getDefault().asyncExec(new Runnable(){
			@Override
			public void run() {
				IDecoratorManager dManager = PlatformUI.getWorkbench().getDecoratorManager();
				dManager.update(LighthouseProjectLabelDecorator.DECORATOR_ID);
			}});
	}
	
	public static Collection<IFile> getFilesFromJavaProject(IJavaProject jProject){
		Collection<IFile> files = new HashSet<IFile>();
		try {
			IPackageFragment[]  packagesFragments = jProject.getPackageFragments();
			for (IPackageFragment packageFragment: packagesFragments){
				if (packageFragment.getKind() == IPackageFragmentRoot.K_SOURCE && packageFragment.getCompilationUnits().length > 0) {
					ICompilationUnit[] icus = packageFragment.getCompilationUnits();
					for(ICompilationUnit icu : icus){
						files.add((IFile) icu.getResource());
					}
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return files;
	}
	
	public static String getMetadataDirectory() {
		return ResourcesPlugin.getWorkspace().getRoot().getLocation().toPortableString()  + "/.metadata/";
	}
	
	public static String[] getSourceFolders(IJavaProject project) {
		List<String> result = new LinkedList<String>();
		try {
			IClasspathEntry[] classPaths = project.getRawClasspath();
			for (IClasspathEntry cp : classPaths) {
				if (cp.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
					result.add(cp.getPath().toPortableString());
				}
			}
		} catch (JavaModelException e) {
			logger.error(e,e);
		}
		return result.toArray(new String[0]);
	}
	
	public static Map<IFile, ISVNInfo> getSVNInfoFromWorkspace(){
		Map<IFile, ISVNInfo> result = new HashMap<IFile, ISVNInfo>();
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
									/*String fqn = ModelUtility
									.getClassFullyQualifiedName(iFile);
									if (fqn != null) {*/
										/*Date revision = svnInfo
										.getLastChangedDate();
										if (revision == null) {
											revision = new Date(0);
										} else {
											revision = new Date(revision
													.getTime());
										}*/
										result.put(iFile, svnInfo);
									//}
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
	
	public static ISVNInfo getSVNInfo(IFile iFile) {
		ISVNInfo svnInfo = null;
		try {
			ISVNClientAdapter svnAdapter = SVNProviderPlugin.getPlugin()
					.getSVNClient();
			svnInfo = svnAdapter.getInfoFromWorkingCopy(iFile.getLocation()
					.toFile());
		} catch (Exception e) {
			logger.error(e, e);
		}
		return svnInfo;
	}
}
