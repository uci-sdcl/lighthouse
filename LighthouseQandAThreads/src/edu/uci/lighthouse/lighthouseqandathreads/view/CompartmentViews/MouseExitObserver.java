package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import java.awt.MouseInfo;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Mouse Exit events are send every x ms so it is possible to move the mouse fast outside of a swt shell
 * and the widget will never receive the mouse exit event. This class constantly monitors the mous coordinates
 * and will always know when the mouse has exited a swt shell. 
 * @author lee
 *
 */
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

			if (!shell.isDisposed())
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
