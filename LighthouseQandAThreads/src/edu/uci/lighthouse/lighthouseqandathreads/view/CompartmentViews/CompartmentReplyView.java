package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import edu.uci.lighthouse.lighthouseqandathreads.PersistAndUpdate;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.LHforum;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class CompartmentReplyView extends Composite{
	private int NUM_COLUMNS = 1;
	
	public CompartmentReplyView(Composite parent, int style, ForumThread thread, TeamMember tm, PersistAndUpdate pu){
		super(parent,style);
		
		this.setLayout(new GridLayout(1, false));
		
		Color threadBack2 = new Color(this.getDisplay(),231,232,130);
		
		this.setBackground(threadBack2);
		
		Label post = new Label(this,SWT.None);
		post.setText("Question: \n"+thread.getRootQuestion().getMessage());
		post.setBackground(threadBack2);
		
		ReplyBoxView view = new ReplyBoxView(this, SWT.None,thread,tm, pu);
	}

}

