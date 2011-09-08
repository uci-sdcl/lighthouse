package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import java.awt.Event;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

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

		messageLabel = new Label(message.length() >= displayLength ? ""+ message.substring(0,displayLength) +"...": ""+ message);
		
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


			treadShell.setLocation(point);
			treadShell.addMouseTrackListener(new ThreadTrackListener());
			
			treadShell.setLayout(new org.eclipse.swt.layout.GridLayout(1, false));
			
			
			
			
			view = new CompartmentThreadView(treadShell,SWT.None, thread,tm,pu);
			
			
			treadShell.setSize(treadShell.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			
			ViewManager viewMan = ViewManager.getInstance();
			viewMan.addCompartmentThreadView(view);
			viewMan.addOpenThread(thread);
			
			if(!treadShell.isDisposed())
				treadShell.open();
			}

		
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
		public void mouseExit(org.eclipse.swt.events.MouseEvent e) {
			org.eclipse.swt.graphics.Point point = new org.eclipse.swt.graphics.Point(e.x,e.y);
	
			
			if(!treadShell.isDisposed()){
			
				for(Control control : treadShell.getChildren()){
					if(containsPoint(control,point))
						return;
				}
			}
			
			if(!shellContainsPoint(treadShell,point) && !treadShell.isDisposed() && !view.isPin()){
				treadShell.close();
				ViewManager.getInstance().clearViews();
				ViewManager.getInstance().removeOpenThread(thread);
			}
			
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
		
		
		/**
		 * Counts bounder width as well
		 * 
		 * @param control
		 * @param point
		 * @return
		 */
		public boolean shellContainsPoint(Control control, org.eclipse.swt.graphics.Point point){
			if(control.isDisposed())
				return false;
			
			Rectangle bounds = control.getBounds();
			int borderWidth = control.getBorderWidth();
			int menuHeight = 0;
			Menu bar = treadShell.getMenuBar();

			Method getBoundsMeth;
			Rectangle menuRect = null;
			
			try {
				getBoundsMeth = Menu.class.getDeclaredMethod("getBounds", null);
				getBoundsMeth.setAccessible(true);
				menuRect = (Rectangle)getBoundsMeth.invoke(bar, null);
				menuHeight = menuRect.height;
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			bounds.height = bounds.height+borderWidth+menuHeight;
			
			bounds.width = bounds.width+borderWidth;

			//System.out.println(menuRect.x +", "+menuRect.y+", "+menuRect.width+", "+menuRect.height);
			org.eclipse.swt.graphics.Point point2 = treadShell.toDisplay(point);
			//System.out.println(point2.x+", "+point2.y);
			
			return (bounds.contains(point) || menuRect.contains(point2));
		}
		
		public void mouseHover(org.eclipse.swt.events.MouseEvent e) {
		}
	
	}
}
