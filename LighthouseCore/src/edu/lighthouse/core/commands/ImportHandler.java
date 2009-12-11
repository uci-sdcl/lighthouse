package edu.lighthouse.core.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.uci.lighthouse.core.controller.PushModel;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;

public class ImportHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil
		.getActiveMenuSelection(event);
		
		final Collection<IFile> javaFiles = new ArrayList<IFile>();
		
		for (Iterator iterator = selection.iterator(); iterator.hasNext();) {
			Object itemSelected =  iterator.next();
			if (itemSelected instanceof IJavaProject) {
				IJavaProject jProject = (IJavaProject) itemSelected;
				/*  Verifies if the project is open in the workspace. This method is different from IJavaProject.isOpen() */
				if (jProject.getProject().isOpen()) { 
					javaFiles.addAll(getFilesFromJavaProject(jProject));
				} // TODO handle else
			}
		}
		
		final Job job = new Job("Importing Project") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try{ 
				monitor.beginTask("Importing files...", javaFiles.size());
				PushModel pushModel = new PushModel(LighthouseModel.getInstance());
				Collection<LighthouseEvent> listEvents = pushModel.ParseJavaFiles(javaFiles);
				pushModel.saveEventsInDatabase(listEvents, new SubProgressMonitor(monitor, javaFiles.size(),SubProgressMonitor.PREPEND_MAIN_LABEL_TO_SUBTASK));
//					if (monitor.isCanceled()) return Status.CANCEL_STATUS;
				} catch (Exception e) {
					//TODO: UI
				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
		
		
		// FIXME
////		Shell shell = PlatformUI.getWorkbench()
////		.getActiveWorkbenchWindow().getShell();
//		
//		PushModel pushModel = new PushModel(LighthouseModel.getInstance());
//		try {
//			pushModel.importJavaFiles(javaFiles);
//			LighthouseModel.getInstance().fireModelChanged();
////			MessageDialog.openInformation(shell,"Lighthouse", "Project imported successfully!");
//		} catch (Exception e) {
////			MessageDialog.openError(shell,"Database Connection", "Imposible to connect to server. Please, check your connection settings.");
//		}
//		
		return null;
	}
	
	private Collection<IFile> getFilesFromJavaProject(IJavaProject jProject){
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

}
