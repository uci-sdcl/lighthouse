package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import java.util.ArrayList;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import edu.uci.lighthouse.lighthouseqandathreads.PersistAndUpdate;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.TeamMember;
import edu.uci.lighthouse.ui.utils.GraphUtils;

public class CompartmentRootPostView extends Panel {

	private int displayLength = 100;
	private int NUM_COLUMNS = 2;
	private Label messageLabel;
	private String prefix = "? ";
	private Shell treadShell;
	private ForumThread thread;
	private TeamMember tm;
	private PersistAndUpdate pu;

	public CompartmentRootPostView(String message,  ForumThread thread, TeamMember tm, PersistAndUpdate pu) {

		this.tm = tm;
		
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.numColumns = NUM_COLUMNS;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		this.pu = pu;

		setLayoutManager(layout);
		
		String author = thread.getRootQuestion().getTeamMemberAuthor().getAuthor().getName();
		


		messageLabel = new Label(message.length() >= displayLength ? author + " " + message.substring(0,displayLength) : author + " " + message);
		this.add(messageLabel);

		PostMouseMotionListener pl = new PostMouseMotionListener();

		this.addMouseMotionListener(pl);
		messageLabel.addMouseMotionListener(pl);
		
		this.thread = thread;
	}

	private class PostMouseMotionListener extends MouseMotionListener.Stub {
		public void mouseHover(MouseEvent me) {
			treadShell = new Shell(GraphUtils.getGraphViewer()
					.getGraphControl().getDisplay().getActiveShell(),
					SWT.NO_TRIM | SWT.DRAG | SWT.RESIZE);
		//	treadShell.setSize(100, 200);
			Point location = me.getLocation();
			org.eclipse.swt.graphics.Point point = GraphUtils.getGraphViewer()
					.getGraphControl().toDisplay(location.x, location.y);

			treadShell.setLocation(point);
			treadShell.addMouseTrackListener(new ThreadTrackListener());
			
			treadShell.setLayout(new org.eclipse.swt.layout.GridLayout(1, false));
			
			
			
			CompartmentThreadView view = new CompartmentThreadView(treadShell,SWT.None, thread,tm,pu);
			
			
			treadShell.setSize(treadShell.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			
			treadShell.open();
			

		
		}

		public void mouseExited(MouseEvent me) {
		}

		public void mouseEntered(MouseEvent me) {
			//CompartmentPostView.this.setBackgroundColor(ColorConstants.yellow);


		}
	}
	
	private class ThreadTrackListener implements MouseTrackListener {

		
		public void mouseEnter(org.eclipse.swt.events.MouseEvent e) {
		}
		public void mouseExit(org.eclipse.swt.events.MouseEvent e) {
			//CompartmentPostView.this.setBackgroundColor(null);
			//treadShell.close();
			
		}
		public void mouseHover(org.eclipse.swt.events.MouseEvent e) {
		}
	
	}
}
