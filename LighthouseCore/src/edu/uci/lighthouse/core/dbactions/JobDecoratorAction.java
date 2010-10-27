package edu.uci.lighthouse.core.dbactions;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;

import edu.uci.lighthouse.core.Activator;
import edu.uci.lighthouse.model.jpa.JPAException;

public class JobDecoratorAction implements IDatabaseAction {

	private static final long serialVersionUID = -4080828887416287463L;

	private IDatabaseAction action;
	
	private String titleLabel;
	
	private String taskLabel;
	
	private static Logger logger = Logger.getLogger(JobDecoratorAction.class);
	
	private Job job;
	
	public JobDecoratorAction(IDatabaseAction action) {
		this(action, "Lighthouse Database Actions", "Executing "+action.getClass().getSimpleName()+" ...");
	}
	
	public JobDecoratorAction(IDatabaseAction action, String titleLabel, String taskLabel) {
		this.action = action;
		this.titleLabel = titleLabel;
		this.taskLabel = taskLabel;
		attachJob();
	}
	
	@Override
	public void run() {
		job.setUser(true);
		job.schedule();
	}
	
	private void attachJob() {
		job = new Job(titleLabel) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					monitor.beginTask(taskLabel,
							IProgressMonitor.UNKNOWN);
					action.run();
				} catch (JPAException e) {
					logger.error(e, e);
					return new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage());
				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}
		};
//		job.addJobChangeListener(new JobChangeAdapter() {
//			@Override
//			public void done(final IJobChangeEvent event) {
//				if (event.getResult() != Status.CANCEL_STATUS && !event.getResult().isOK()) {
//					UserDialog.openError(event.getResult().getMessage());
//				}
//			}
//		});
	}
	
	public void addJobChangeListener(IJobChangeListener listener) {
		job.addJobChangeListener(listener);
	}
}
