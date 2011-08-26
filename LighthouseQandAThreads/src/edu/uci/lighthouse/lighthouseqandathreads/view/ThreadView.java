package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;

import edu.uci.lighthouse.model.QAforums.ForumThread;

public class ThreadView extends ConversationElement{
	
	ArrayList<PostView> postViews = new ArrayList<PostView>();
	private ForumThread thread;

	public ThreadView(Composite parent, int style, ForumThread thread) {
		super(parent, style);
		this.thread = thread;
	}
	
	public void addPostView(PostView postView){
		this.postViews.add(postView);
	}

}
