package edu.uci.lighthouse.core.util;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
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

import edu.uci.lighthouse.core.decorators.LighthouseProjectLabelDecorator;

public class WorkbenchUtility {

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return files;
	}
	
	public static String getMetadataDirectory() {
		return ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString()  + "/.metadata/";
	}
}
