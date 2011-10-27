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

import java.awt.Event;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.lighthouseqandathreads.Activator;
import edu.uci.lighthouse.lighthouseqandathreads.PersistAndUpdate;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.TeamMember;
import edu.uci.lighthouse.ui.utils.GraphUtils;

public class CompartmentRootPostView extends Panel {

	private int displayLength = 22;
	private int NUM_COLUMNS = 1;
	private Label messageLabel;
	private String prefix = "? ";
	private Shell treadShell;
	private ForumThread thread;
	private TeamMember tm;
	private PersistAndUpdate pu;
	private CompartmentThreadView view;
	
	
	public CompartmentRootPostView(String message,  ForumThread thread, TeamMember tm, PersistAndUpdate pu) {

		this.tm = tm;
		this.thread = thread;
		this.pu = pu;
		
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.numColumns = NUM_COLUMNS;
		layout.marginHeight = 0;
		layout.marginWidth = 0;

		setLayoutManager(layout);
		
		String author = thread.getRootQuestion().getTeamMemberAuthor().getAuthor().getName();
		
		GridData data = new GridData(SWT.FILL,SWT.FILL,true,true);
		
		
		String msg = new String (message);
		msg = msg.replace('\n', ' ');

		messageLabel = new Label(message.length() >= displayLength ? ""+ msg.substring(0,displayLength) +"...": ""+ msg);
		
		Image icon;
		
		if(thread.isClosed() && thread.hasSolution()){
			icon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
			"/icons/closedAndAnswered.png").createImage();
		}else if(thread.isClosed()){
			icon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
			"/icons/closed.png").createImage();
		}else if(thread.hasSolution()){
			icon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
			"/icons/answer.png").createImage();
		}else if(thread.hasReplies()){
			icon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
			"/icons/questionReply.png").createImage();
		}else {
			icon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
			"/icons/question.png").createImage();
		}
		
		messageLabel.setIcon(icon);
		messageLabel.setIconAlignment(SWT.LEFT);
		this.add(messageLabel);

		PostMouseMotionListener pl = new PostMouseMotionListener();

		this.addMouseMotionListener(pl);
		messageLabel.addMouseMotionListener(pl);
		

	}
	


	private class PostMouseMotionListener extends MouseMotionListener.Stub {
		public void mouseHover(MouseEvent me) {
			
			if(!ViewManager.getInstance().threadOpen(thread)){
			treadShell = new Shell(GraphUtils.getGraphViewer()
					.getGraphControl().getDisplay().getActiveShell(),
					SWT.NO_TRIM | SWT.DRAG | SWT.RESIZE);

			Point location = me.getLocation();
			org.eclipse.swt.graphics.Point point = GraphUtils.getGraphViewer()
					.getGraphControl().toDisplay(location.x, location.y);

			point.x = point.x -10;
			
			treadShell.setLocation(point);
			treadShell.addMouseTrackListener(new ThreadTrackListener());
			
			org.eclipse.swt.layout.GridLayout layout = new org.eclipse.swt.layout.GridLayout(1, false);
			layout.horizontalSpacing = 1;
			layout.verticalSpacing = 1;
			layout.marginWidth = 1;
			layout.marginHeight = 1;
			
			treadShell.setLayout(layout);
			
			
			
			
			view = new CompartmentThreadView(treadShell,SWT.None, thread,tm,pu);
			
			
			treadShell.setSize(treadShell.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			
			ViewManager viewMan = ViewManager.getInstance();
			viewMan.addCompartmentThreadView(view);
			viewMan.addOpenThread(thread);
			
			
			
			
			
			if(!treadShell.isDisposed())
				treadShell.open();
			}

			
			MouseExitObserver mo = new MouseExitObserver(treadShell,thread,view);
			
			mo.schedule();
		
		}

		public void mouseExited(MouseEvent e) {

			
		}

		public void mouseEntered(MouseEvent me) {
			//CompartmentPostView.this.setBackgroundColor(ColorConstants.yellow);


		}
	}
	

	
	private class ThreadTrackListener implements MouseTrackListener {
		
		public void mouseEnter(org.eclipse.swt.events.MouseEvent e) {
		}
		/**
		 * Mouse events are reported every X ms, so... what to do if the mouse
		 * goes to fast?
		 */
		public void mouseExit(org.eclipse.swt.events.MouseEvent e) {
		/*	org.eclipse.swt.graphics.Point point = new org.eclipse.swt.graphics.Point(e.x,e.y);
	
			
			if(!treadShell.isDisposed()){
			
				for(Control control : treadShell.getChildren()){
					if(containsPoint(control,point))
						return;
				}
			}
			
			if(!containsPoint(treadShell,point) && !treadShell.isDisposed() && !view.isPin()){
				treadShell.close();
				ViewManager.getInstance().clearViews();
				ViewManager.getInstance().removeOpenThread(thread);
			}*/
			
		}
		
		public boolean containsPoint(Control control, org.eclipse.swt.graphics.Point point){
			if(control.isDisposed())
				return false;
			
			Rectangle bounds = control.getBounds();
			int borderWidth = control.getBorderWidth();

			bounds.height = bounds.height+borderWidth;
			bounds.width = bounds.width+borderWidth;
			
			return bounds.contains(point);
		}
		
		

		
		public void mouseHover(org.eclipse.swt.events.MouseEvent e) {
		}
	
	}
}
