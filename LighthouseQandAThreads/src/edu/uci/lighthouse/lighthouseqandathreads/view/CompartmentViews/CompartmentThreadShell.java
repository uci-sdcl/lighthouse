package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import org.eclipse.draw2d.Panel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.widgets.Shell;

public class CompartmentThreadShell extends Shell{

	public CompartmentThreadShell(Shell activeShell, int noTrim) {
		super(activeShell, noTrim);
		
		PostMouseMotionListener listener = new PostMouseMotionListener();
		this.addMouseTrackListener(listener);
	}

	
	private class PostMouseMotionListener implements MouseTrackListener {

		public void mouseEnter(MouseEvent e) {			
		}
		public void mouseExit(MouseEvent e) {
			CompartmentThreadShell.this.close();
		}
		public void mouseHover(MouseEvent e) {			
		}
	
	}
}
