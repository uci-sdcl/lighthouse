package edu.uci.lighthouse.core.util;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * Thread safe MessageDialog.
 * 
 * @author tproenca
 *
 */
public class UserDialog {

	public static void openError(final String message){
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				Shell shell = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell();
				MessageDialog.openError(shell,"Lighthouse", message);
			}
		});
	}
	
	public static void openInformation(final String message){
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				Shell shell = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell();
				MessageDialog.openInformation(shell,"Lighthouse", message);
			}
		});
	}
}
