package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

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

public class ThreadView extends ConversationElement implements IHasObservablePoint, Observer{
	
	private ForumThread thread;
	private TeamMember tm;
	private int height = 30;

	
	public ThreadView(Composite parent, int style, ForumThread thread, TeamMember tm) {
		super(parent, style);
		this.thread = thread;
			this.tm = tm;
	      GridData compsiteData = new GridData();

			this.setLayout(new GridLayout(1, false));
			this.setLayoutData(compsiteData);
			this.setBackground(ColorConstants.white);
		
		
			addPost(thread.getRootQuestion());
	
	}
	

	public ForumThread getThread(){
		return thread;
	}
	
	public void addPost(Post post){
		
		addPostView(post,  tm, GridData.HORIZONTAL_ALIGN_BEGINNING);
		addResponsePosts(post.getResponses());
	}
	
	public void addResponsePosts(Set<Post> posts){

		
		for(Post post: posts){
			
			addPostView(post,  tm, GridData.HORIZONTAL_ALIGN_END);
			
			for(Post childPost: post.getResponses()){
				addPost(childPost);
			}

		}


	}
	
	private void addPostView(Post post, TeamMember tm, int alignment){
		PostView pv = new PostView(this, SWT.None, post,tm);
		pv.observeMe(this);
		GridData data = new GridData();
		data.horizontalAlignment = alignment;
		this.setSize(this.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		this.layout();
	}
	




	@Override
	public void update(Observable o, Object arg) {
		this.getObservablePoint().changed(this);
		
	}
	


}
