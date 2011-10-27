/*******************************************************************************
* Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
*				, University of California, Irvine}.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    {Software Design and Collaboration Laboratory (SDCL)
*	, University of California, Irvine}
*			- initial API and implementation and/or initial documentation
*******************************************************************************/
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
	private ForumThread thread;
	ReplyBoxView view;
	
	public CompartmentReplyView(Composite parent, int style, ForumThread thread, TeamMember tm, PersistAndUpdate pu){
		super(parent,style);
		
		this.setLayout(new GridLayout(1, false));
		this.setThread(thread);
		
		Color threadBack2 = new Color(this.getDisplay(),231,232,130);
		
		this.setBackground(threadBack2);
		
		Label post = new Label(this,SWT.None);
		post.setText("Question: \n"+thread.getRootQuestion().getMessage());
		post.setBackground(threadBack2);
		
		view = new ReplyBoxView(this, SWT.None,thread,tm, pu);
	}

	public void setThread(ForumThread thread) {
		this.thread = thread;
		if(view != null)
			view.setThread(thread);
		
	}

	public ForumThread getThread() {
		return thread;
	}

}

