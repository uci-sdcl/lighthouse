package edu.uci.lighthouse.extensions.codereview.ui;

import org.eclipse.mylyn.internal.provisional.commons.ui.AbstractNotificationPopup;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * @author tproenca
 *
 */
public class RequestNotification extends AbstractNotificationPopup {
	
	private static int CLOSE_DELAY = 3000;

	public RequestNotification(Display display) {
		super(display);
		setDelayClose(RequestNotification.CLOSE_DELAY);
	}

	@Override
	protected void createContentArea(Composite parent) {
		// TODO Auto-generated method stub
		super.createContentArea(parent);
	}

	@Override
	protected String getPopupShellTitle() {
		// TODO Auto-generated method stub
		return "Review Request Notification";
	}

}
