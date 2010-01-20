package edu.uci.lighthouse.core.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.uci.lighthouse.core.Activator;
import edu.uci.lighthouse.core.controller.PushModel;
import edu.uci.lighthouse.core.util.UserDialog;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.parser.ParserException;

public class ImportHandler extends AbstractHandler {

	private static Logger logger = Logger.getLogger(ImportHandler.class);
	
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
				// Parsing files
				monitor.subTask("Parsing Java files...");
				Collection<LighthouseEvent> listEvents = pushModel.ParseJavaFiles(javaFiles);
				// Saving in the Database (it has its own monitor).
				monitor.subTask("Saving data to the database...");
				pushModel.saveEventsInDatabase(listEvents, new SubProgressMonitor(monitor,javaFiles.size()));
				//TODO: Rollback model changes
				LighthouseModel.getInstance().fireModelChanged();
				} catch (final JPAException e) {
					logger.error(e);
					UserDialog.openError("JPAException: "+e.getMessage());
				} catch (final ParserException e) {
					logger.error(e);
					UserDialog.openError("ParserException: "+e.getMessage());
				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}
		};
		job.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void done(final IJobChangeEvent event) {
				if (event.getResult() != Status.CANCEL_STATUS && !event.getResult().isOK()) {
					UserDialog.openError("Imposible to connect to server. Please, check your connection settings.");
				}
			}
		});
		job.setUser(true);
		job.schedule();

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
