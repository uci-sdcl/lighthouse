package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;

public class ThreadView extends ConversationElement{
	
	ArrayList<PostView> postViews = new ArrayList<PostView>();

	public ThreadView(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}
	
	public void addPostView(PostView postView){
		this.postViews.add(postView);
	}

}
