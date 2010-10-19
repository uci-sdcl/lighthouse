package edu.uci.lighthouse.core.dbactions.pull;

import edu.uci.lighthouse.core.controller.WorkingCopy;
import edu.uci.lighthouse.core.util.WorkbenchUtility;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.jpa.JPAException;

public class SynchronizeModelAction extends AbstractWorkingCopyAction {

	private static final long serialVersionUID = 813072327005798103L;

	public SynchronizeModelAction(WorkingCopy workingCopy) {
		super(workingCopy);
	}

	@Override
	public void run() throws JPAException {
		LighthouseModel.getInstance().clear();
		CheckoutAction checkoutAction = new CheckoutAction(getWorkingCopy());
		checkoutAction.run();
		WorkbenchUtility.updateProjectIcon();
	}

}
