package edu.uci.lighthouse.lighthouseqandathreads;

import java.util.List;

import edu.uci.lighthouse.LHmodelExtensions.LHclassPluginExtension;
import edu.uci.lighthouse.core.controller.Controller;
import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.lighthouseqandathreads.view.NewQuestionDialog;
import edu.uci.lighthouse.lighthouseqandathreads.view.VisualSummaryView;
import edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews.CompartmentRootPostView;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.LHforum;
import edu.uci.lighthouse.model.QAforums.LHthreadCreator;
import edu.uci.lighthouse.model.QAforums.TeamMember;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.LHEntityDAO;
import edu.uci.lighthouse.ui.figures.CompartmentFigure;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure.MODE;
import edu.uci.lighthouse.ui.figures.CompartmentFigure;
import edu.uci.lighthouse.ui.utils.GraphUtils;
import edu.uci.lighthouse.ui.views.EmergingDesignView;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.Panel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.draw2d.ActionListener;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;

import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;

import org.eclipse.draw2d.MouseListener;

import org.eclipse.draw2d.Label;

public class ThreadFigure extends CompartmentFigure {

	private FlowLayout layout;
	private Image icon;
	
	private LighthouseEntity le;
    private LighthouseClass clazz;
	private LHforum forum;
	private int NUM_COLUMNS = 1;

	static{
	LighthouseQAEventSubscriber subscriber = new LighthouseQAEventSubscriber();
	Controller.getInstance().subscribeToLighthouseEvents(subscriber);
	}
	
	MODE mode;


	public ThreadFigure() {
		GridLayout layout = new GridLayout();
		//layout.horizontalSpacing = 0;
		//layout.verticalSpacing = 0;
		layout.numColumns = NUM_COLUMNS;			
		//layout.marginHeight = 0;
		//layout.marginWidth = 0; 
		setLayoutManager(layout);
		
		icon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
				"/icons/question.png").createImage();
		
		
		
	//	this.addMouseMotionListener(new Listener());

	}

	public boolean isVisible(MODE mode) {
		return true;
	}
	
	private void decidedToHideOrNot(int count,QuestionPanel panel){
		if(count == 0){
			panel.setVisible(false);
		}
		else{
			panel.setVisible(true);
		}
	}

	public void populate(MODE mode) {
		
		le = getUmlClass();			
	    clazz = (LighthouseClass) le;
		forum = clazz.getForum();
		if(forum == null){
			forum = new LHforum();
			clazz.setForum(forum);		
		}
	
		
		
	/*	VisualSummaryView vsv = 
			new VisualSummaryView(forum.countThreads(),forum.countSolvedThreads(),forum.countTotalResonses());

		
		QuestionPanel questPanel = new QuestionPanel(vsv);
		
		decidedToHideOrNot(forum.countThreads(), questPanel);
		
		this.add(questPanel); */
		LighthouseAuthor author = ModelUtility.getAuthor();
		LHthreadCreator tm = new LHthreadCreator(author);
		PersistAndUpdate pu = new PersistAndUpdate(clazz);
		for(ForumThread thread: forum.getThreads()){
			CompartmentRootPostView postView = new CompartmentRootPostView(thread.getRootQuestion().getMessage(), thread,tm,pu);
			GridData data = new GridData(SWT.FILL,SWT.FILL,true,true);
			//data.horizontalSpan = 2;
			this.add(postView,data);
			
			
		}
	
		
		
	}
	
	private class Listener implements MouseMotionListener{


		@Override
		public void mouseDragged(MouseEvent arg0) {
						
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			
			MenuManager menuMgr = new MenuManager("#Q&A Menu");
			menuMgr.setRemoveAllWhenShown(true);
			menuMgr.addMenuListener(new IMenuListener() {
				public void menuAboutToShow(IMenuManager manager) {
					QAcontextMenuAction qaAction = new QAcontextMenuAction(forum, clazz, le);
					manager.add(qaAction);
					
				}
			});
			
			GraphViewer viewer = GraphUtils.getGraphViewer();
			
			Menu menu = menuMgr.createContextMenu(viewer.getControl());
			viewer.getControl().setMenu(menu);
			IViewReference ref =
				PlatformUI.getWorkbench().
				getActiveWorkbenchWindow().getActivePage().findViewReference(EmergingDesignView.Plugin_ID);
			EmergingDesignView view = (EmergingDesignView)ref.getView(false);

			view.getSite().registerContextMenu(menuMgr, viewer);	
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseHover(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	

	

	private class QuestionPanel extends Panel {
		private FlowLayout QuestionPanel_layout;

		public QuestionPanel(VisualSummaryView vsv) {
			QuestionPanel_layout = new FlowLayout();
			QuestionPanel_layout.setMajorAlignment(FlowLayout.ALIGN_LEFTTOP);
			QuestionPanel_layout.setMinorSpacing(25);
			setLayoutManager(QuestionPanel_layout);
		
			this.add(vsv);
		}


	}

}
