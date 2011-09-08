package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import java.awt.MouseInfo;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class MouseExitObserver implements Runnable {

	Shell shell;

	MouseExitObserver(Shell shell) {
		this.shell = shell;
	}

	@Override
	public void run() {
		boolean exit = false;
		while (!exit) {
			java.awt.Point mouseLocation = MouseInfo.getPointerInfo()
					.getLocation();
			final int x = mouseLocation.x;
			final int y = mouseLocation.y;

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

					if (!childHasIt && !shell.isDisposed()
							&& !containsPoint(shell, point)) {
						shell.close();
					}

				}

			});

			if (shell.isDisposed())
				exit = true;

		}

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
