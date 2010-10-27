package edu.uci.lighthouse.core.dbactions;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import edu.uci.lighthouse.core.util.UserDialog;
import edu.uci.lighthouse.model.jpa.JPAException;

public class JobDecoratorAction implements IDatabaseAction{

	private IDatabaseAction action;
	
	private String titleLabel;
	
	private String taskLabel;
	
	private static Logger logger = Logger.getLogger(JobDecoratorAction.class);
	
	public JobDecoratorAction(IDatabaseAction action) {
		this(action, "Lighthouse Database Actions", "Executing "+action.getClass().getName()+" ...");
	}
	
	public JobDecoratorAction(IDatabaseAction action, String titleLabel, String taskLabel) {
		this.action = action;
		this.titleLabel = titleLabel;
		this.taskLabel = taskLabel;
	}
	
	@Override
	public void run() {
		final Job job = new Job(titleLabel) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					monitor.beginTask(taskLabel,
							IProgressMonitor.UNKNOWN);
					action.run();
				} catch (JPAException e) {
					logger.error(e, e);
					UserDialog.openError("JPAException: " + e.getMessage());
				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
	}
}
