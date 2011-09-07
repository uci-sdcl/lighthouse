package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Panel;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.zest.core.viewers.GraphViewer;

import edu.uci.lighthouse.core.controller.Controller;
import edu.uci.lighthouse.core.data.ISubscriber;
import edu.uci.lighthouse.lighthouseqandathreads.ForumController;
import edu.uci.lighthouse.lighthouseqandathreads.PersistAndUpdate;
import edu.uci.lighthouse.lighthouseqandathreads.PostController;
import edu.uci.lighthouse.lighthouseqandathreads.actions.AnswerMenuAction;
import edu.uci.lighthouse.lighthouseqandathreads.actions.CloseThreadMenuAction;
import edu.uci.lighthouse.lighthouseqandathreads.actions.QAcontextMenuAction;
import edu.uci.lighthouse.lighthouseqandathreads.actions.ReplyMenuAction;
import edu.uci.lighthouse.lighthouseqandathreads.view.ForumElement;
import edu.uci.lighthouse.lighthouseqandathreads.view.ForumView;
import edu.uci.lighthouse.lighthouseqandathreads.view.LayoutMetrics;
import edu.uci.lighthouse.lighthouseqandathreads.view.ListComposite;
import edu.uci.lighthouse.lighthouseqandathreads.view.RespondBoxView;

import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;
import edu.uci.lighthouse.ui.utils.GraphUtils;
import edu.uci.lighthouse.ui.views.EmergingDesignView;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class CompartmentThreadView extends Composite implements ISubscriber{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6013962858601965104L;
	
	//final StyledText postNewThreadBox;
	private String reply = "";
	private Composite replyComposite;
	private ForumThread thread;
	private TeamMember tm;
	private PersistAndUpdate pu;
	private ListComposite listOfReplies;
	private ScrolledComposite scroller;
	
	public CompartmentThreadView(Composite parent, int style, ForumThread thread, TeamMember tm, PersistAndUpdate pu) {
		super(parent, style);

		this.setLayout(new GridLayout(1, false));
		Color threadBack2 = new Color(this.getDisplay(),231,232,130);
		
		this.setBackground(threadBack2);
		this.thread = thread;
		this.tm = tm;
		this.pu = pu;
		
		
		scroller = new ScrolledComposite(this,SWT.V_SCROLL | SWT.H_SCROLL);
		GridData scrollData = new GridData(LayoutMetrics.CONVERSATION_LIST_WIDTH, LayoutMetrics.CONVERSATION_LIST_HEIGHT);
		scrollData.horizontalAlignment = GridData.CENTER;
		scroller.setLayoutData(scrollData);
		scroller.setLayout(new GridLayout(1, false));
		
		

		
		listOfReplies = new ListComposite(scroller,SWT.None);
		scroller.setContent(listOfReplies);
		scroller.setMinSize(listOfReplies.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		Color postBack = new Color(this.getDisplay(), 255, 212, 102);
		Color scrollerBack = new Color(this.getDisplay(),231,232,130);
		scroller.setBackground(scrollerBack);
		

		
		
		//root question

		TeamMember poster = thread.getRootQuestion().getTeamMemberAuthor();
		
		CompartmentPostView cpv = new CompartmentPostView(this,SWT.None, thread.getRootQuestion(),poster, false);


		
		listOfReplies.add(cpv);
		listOfReplies.setSize(listOfReplies.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		listOfReplies.renderList();
		
		
		
		//---add reply posts
		for(Post post : thread.getRootQuestion().getResponses()){
			

			poster = post.getTeamMemberAuthor();
			CompartmentPostView cpv2 = new CompartmentPostView(this,SWT.None, post,poster, true);

			
			listOfReplies.add(cpv2);
			listOfReplies.setSize(listOfReplies.computeSize(SWT.DEFAULT, SWT.DEFAULT));

			listOfReplies.renderList();
		}
		



		setMenu(this);
	}
	
	
	private void setMenu(Control control){
		MenuManager menuMgr = new MenuManager("#Reply");
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				
				AnswerMenuAction ama = new AnswerMenuAction(thread,tm,pu);
				manager.add(ama);
				
				manager.add( new Separator());
				
				ReplyMenuAction rmAction = new ReplyMenuAction(thread,tm,pu);
				manager.add(rmAction);
				

				
				CloseThreadMenuAction ctma = new CloseThreadMenuAction(thread,tm,pu);
				manager.add(ctma);
				
			}
		});
		menuMgr.setRemoveAllWhenShown(true);

		Menu menu1 = menuMgr.createContextMenu(control);
		
		control.setMenu(menu1);
	
		
		 
	    if(control instanceof Composite){
	    	Composite parent = (Composite)control;
			for(Control child : parent.getChildren()){
			   setMenu(child);
			}
			
	    }
	    

		
		
		

		
	}
	
	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getReply() {
		return reply;
	}
	

	

	
	public void updateView(){
		listOfReplies.clearChildren();
		for(Post post : thread.getRootQuestion().getResponses()){	
			ForumElement composite = new ForumElement(this, SWT.None);
			GridData data = new GridData(LayoutMetrics.POST_VIEW_WIDTH,
				LayoutMetrics.POST_VIEW_HEIGHT);
			composite.setLayoutData(data);
			composite.setLayout(new GridLayout(1,false));
			Label replyLabel = new Label(composite, SWT.None);
			replyLabel.setText(post.getMessage());		
			
			listOfReplies.add(composite);
			listOfReplies.renderList();
		}
		listOfReplies.setSize(listOfReplies.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		this.layout();
	}

	@Override
	public void receive(List<LighthouseEvent> events) {
		updateView();
		
	}
	

}
