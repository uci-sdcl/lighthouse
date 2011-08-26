package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.ArrayList;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import edu.uci.lighthouse.model.QAforums.ForumThread;

public class ThreadView extends ConversationElement{
	
	ArrayList<PostView> postViews = new ArrayList<PostView>();
	private ForumThread thread;

	public ThreadView(Composite parent, int style, ForumThread thread) {
		super(parent, style);
		this.thread = thread;
		
	      GridData compsiteData = new GridData(400, 50);

			this.setLayout(new GridLayout(1, false));
			this.setLayoutData(compsiteData);
			this.setBackground(ColorConstants.white);
		PostView pv = new PostView(this, SWT.None, thread.getRootQuestion());
	}
	
	public void addPostView(PostView postView){
		this.postViews.add(postView);
	}

}
