package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.ArrayList;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.MouseEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.events.MouseListener;

import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class ThreadView extends ConversationElement{
	
	ArrayList<PostView> postViews = new ArrayList<PostView>();
	private ForumThread thread;
	private TeamMember tm;
	
	public ThreadView(Composite parent, int style, ForumThread thread, TeamMember tm) {
		super(parent, style);
		this.thread = thread;
			this.tm = tm;
	      GridData compsiteData = new GridData(550, 30);

			this.setLayout(new GridLayout(1, false));
			this.setLayoutData(compsiteData);
			this.setBackground(ColorConstants.white);
		
			addPost(thread.getRootQuestion());
	
	}
	
	public void addPost(Post post){
		PostView pv = new PostView(this, SWT.None, post,tm);
		
		GridData data = (GridData)this.getLayoutData();
		int height = data.heightHint;
		GridData compsiteData = new GridData(550, height+30);
		this.setLayoutData(compsiteData);
		this.layout();
		
		for(Post childPost: post.getResponses()){
			System.out.println(childPost.getMessage());
			addPost(childPost);
		}
	}
	
	public void addPostView(PostView postView){
		this.postViews.add(postView);
	}
	


}
