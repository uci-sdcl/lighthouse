package edu.uci.lighthouse.core.dbactions.cm;

import edu.uci.lighthouse.core.controller.WorkingCopy;
import edu.uci.lighthouse.core.dbactions.IDatabaseAction;

public abstract class AbstractWorkingCopyAction implements IDatabaseAction {

	private WorkingCopy workingCopy;

	public AbstractWorkingCopyAction(WorkingCopy workingCopy) {
		this.workingCopy = workingCopy;
	}

	public WorkingCopy getWorkingCopy() {
		return workingCopy;
	}
}
