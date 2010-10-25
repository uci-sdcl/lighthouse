package edu.uci.lighthouse.core.dbactions.pull;

import edu.uci.lighthouse.core.controller.WorkingCopy;
import edu.uci.lighthouse.model.jpa.JPAException;

public class UpdateAction  extends AbstractWorkingCopyAction {

	private static final long serialVersionUID = -9051697002637243677L;

	public UpdateAction(WorkingCopy workingCopy) {
		super(workingCopy);
	}

	@Override
	public void run() throws JPAException {
		CheckoutAction checkoutAction = new CheckoutAction(getWorkingCopy());
		checkoutAction.run();
	}

}
