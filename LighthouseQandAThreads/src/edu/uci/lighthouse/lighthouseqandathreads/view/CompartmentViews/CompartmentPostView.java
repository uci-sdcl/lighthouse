package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import java.util.ArrayList;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import edu.uci.lighthouse.lighthouseqandathreads.PersistAndUpdate;
import edu.uci.lighthouse.lighthouseqandathreads.actions.AnswerMenuAction;
import edu.uci.lighthouse.lighthouseqandathreads.actions.CloseThreadMenuAction;
import edu.uci.lighthouse.lighthouseqandathreads.actions.ReplyMenuAction;
import edu.uci.lighthouse.lighthouseqandathreads.view.ForumElement;
import edu.uci.lighthouse.lighthouseqandathreads.view.LayoutMetrics;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class CompartmentPostView extends Composite{
	private ForumThread thread;
	private PersistAndUpdate pu;
	private TeamMember tm;
	private Post post;
	public CompartmentPostView(Composite parent, int style, Post post, TeamMember tm, boolean leftSpacer, ForumThread thread, 
			PersistAndUpdate pu) {
		super(parent, style);
		
		this.tm = tm;
		this.thread = thread;
		this.pu = pu;
		this.post = post;
		

		
		this.setLayout(new GridLayout(2,false));
		Color postBack = new Color(this.getDisplay(), 255, 212, 102);
		
		if(leftSpacer){
			addNewSpacer(this,post.isAnswer());
		}

		Label replyLabel = new Label(this, SWT.NONE);
		Label dateLabel = new Label(this,SWT.None);
		dateLabel.setText(post.getPostTime().toString());
		AnnotationBarView barView = new AnnotationBarView(this,SWT.None,replyLabel,dateLabel);
		GridData barData = new GridData(SWT.FILL,SWT.FILL,true,true);
		barView.setLayoutData(barData);
		
		if(!leftSpacer){
			addNewSpacer(this,post.isAnswer());
		}
		
		
		this.setBackground(postBack);
		Color labelBack = new Color(this.getDisplay(),255, 212, 102);
		replyLabel.setBackground(labelBack);
		
		TeamMember poster = post.getTeamMemberAuthor();
		replyLabel.setText(formatMessage(poster.getAuthor().getName()+": "+post.getMessage()));
		
	   
		
		

		
		this.setSize(this.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
		int height = this.getBounds().height;
		
			GridData data = new GridData(LayoutMetrics.POST_VIEW_WIDTH,
					height);
			this.setLayoutData(data);
			


			
		
	    	setMenu(this);
	    
	}
	
	private String formatMessage(String message){
		char[] msgChars = message.toCharArray();
		ArrayList<Character> listOfChars = new ArrayList<Character>();
		
		int wrapLength = 40;
		for(int i = 0; i<msgChars.length; i++){
			listOfChars.add(msgChars[i]);
			if(i%wrapLength == 0 && i!=0){
				listOfChars.add('\n');
			}
		}
		
		char[] formatedList = new char[listOfChars.size()];
		for(int i = 0; i<listOfChars.size(); i++){
			formatedList[i] = listOfChars.get(i);
		}
		
		String formatedMessage = new String(formatedList);
		return formatedMessage;
		
	}
	
	private void setMenu(Control control){
		MenuManager menuMgr = new MenuManager("#Reply");
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				
				AnswerMenuAction ama = new AnswerMenuAction(thread,tm,pu, post);
				manager.add(ama);
				
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
	
	
	private void addNewSpacer(Composite composite, boolean check) {
		Composite spacer = new Composite(composite, SWT.None);
		spacer.setLayout(new RowLayout());
		spacer.setVisible(false);
		GridData rd = new GridData(20, LayoutMetrics.POST_VIEW_HEIGHT);
		
		spacer.setLayoutData(rd);

		
		if(check){
			spacer.setVisible(true);
			Label label = new Label(spacer,SWT.None);
			FontRegistry fr = new FontRegistry(this.getDisplay());
			Color backColor = new Color(this.getDisplay(),255, 212, 102);
			spacer.setBackground(backColor);
			
			ArrayList<FontData> fdList = new ArrayList<FontData>();
			FontData fd = new FontData("Courier New",14,SWT.BOLD);
			fd.setHeight(20);
			fdList.add(fd);
			
			fr.put("checkFont",fdList.toArray(new FontData[0]));
			
			label.setFont(fr.get("checkFont"));
			label.setText("\u2713");
			label.setBackground(backColor);
			
		}
	}

}
