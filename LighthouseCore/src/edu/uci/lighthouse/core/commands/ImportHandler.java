package edu.uci.lighthouse.core.commands;

import java.util.ArrayList;
import java.util.Collection;
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
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.uci.lighthouse.core.controller.PushModel;
import edu.uci.lighthouse.core.util.UserDialog;
import edu.uci.lighthouse.core.util.WorkbenchUtility;
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
					javaFiles.addAll(WorkbenchUtility.getFilesFromJavaProject(jProject));
				} // TODO handle else
			}
		}

		//TODO (tproenca): Converter pra IDatabaseAction
		final Job job = new Job("Importing Project") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try{ 
					monitor.beginTask("Importing files...", javaFiles.size());
					PushModel pushModel = PushModel.getInstance();
					// Parsing files
					monitor.subTask("Parsing Java files...");
					Collection<LighthouseEvent> listEvents = pushModel.parseJavaFiles(javaFiles);
					// Saving in the Database (it has its own monitor).
					monitor.subTask("Saving data to the database...");
					pushModel.importEventsToDatabase(listEvents, new SubProgressMonitor(monitor,javaFiles.size()));
					//TODO: Rollback model changes
					WorkbenchUtility.updateProjectIcon();
					LighthouseModel.getInstance().fireModelChanged();
				} catch (final JPAException e) {
					logger.error(e,e);
					UserDialog.openError("JPAException: "+e.getMessage());
				} catch (final ParserException e) {
					logger.error(e,e);
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

}
