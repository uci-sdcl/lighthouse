package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import java.awt.MouseInfo;

import org.eclipse.swt.widgets.Shell;

public class MouseExitObserver implements Runnable{
	
	Shell shell;
	
	MouseExitObserver(Shell shell){
		this.shell = shell;
	}

	@Override
	public void run() {
		while(true){
			java.awt.Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
			int x = mouseLocation.x;
			int y = mouseLocation.y;
			if(!shell.isDisposed() && !shell.getBounds().contains(x,y)){
				shell.close();
			}
		}
		
	}

}
