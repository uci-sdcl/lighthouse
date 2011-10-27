/*******************************************************************************
* Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
*				, University of California, Irvine}.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    {Software Design and Collaboration Laboratory (SDCL)
*	, University of California, Irvine}
*			- initial API and implementation and/or initial documentation
*******************************************************************************/
package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import java.awt.MouseInfo;
import java.awt.PointerInfo;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import edu.uci.lighthouse.model.QAforums.ForumThread;

/**
 * Mouse Exit events are send every x ms so it is possible to move the mouse fast outside of a swt shell
 * and the widget will never receive the mouse exit event. This class constantly monitors the mous coordinates
 * and will always know when the mouse has exited a swt shell. 
 * @author lee
 *
 */
public class MouseExitObserver extends Job {

	private Shell shell;
	private ForumThread thread;
	private CompartmentThreadView view;
	
	MouseExitObserver(Shell shell, ForumThread thread, CompartmentThreadView view) {
		super("MouseExitObserver");
		this.shell = shell;
		this.thread = thread;
		this.view = view;
		
	}

	protected IStatus run( IProgressMonitor monitor) {
		boolean exit = false;
		while (!exit) {
			PointerInfo pInfo = MouseInfo.getPointerInfo();
			
			if(pInfo == null)
				continue;
			
			java.awt.Point mouseLocation = pInfo.getLocation();
			
			final int x = mouseLocation.x;
			final int y = mouseLocation.y;

			if (shell != null && !shell.isDisposed())
			shell.getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					boolean childHasIt = false;
					org.eclipse.swt.graphics.Point point = new org.eclipse.swt.graphics.Point(
							x, y);

					if (!shell.isDisposed()) {
						for (Control control : shell.getChildren()) {
							if (containsPoint(control, point))
								childHasIt = true;
						}
					}

					if (!view.isPin() && !childHasIt && !shell.isDisposed()
							&& !containsPoint(shell, point)) {
						shell.close();
						ViewManager.getInstance().clearViews();
						ViewManager.getInstance().removeOpenThread(thread);
					}

				}

			});

			if (shell != null && shell.isDisposed())
				exit = true;

		}
		return Status.OK_STATUS;

	}

	public boolean containsPoint(Control control,
			org.eclipse.swt.graphics.Point point) {
		if (control.isDisposed())
			return false;

		Rectangle bounds = control.getBounds();
		int borderWidth = control.getBorderWidth();

		bounds.height = bounds.height + borderWidth;
		bounds.width = bounds.width + borderWidth;

		return bounds.contains(point);
	}

}
