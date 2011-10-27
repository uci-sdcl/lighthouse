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
package edu.uci.lighthouse.lighthouseqandathreads.actions;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.lighthouseqandathreads.PersistAndUpdate;
import edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews.CompartmentNewPostView;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.LHforum;
import edu.uci.lighthouse.model.QAforums.LHthreadCreator;
import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;
import edu.uci.lighthouse.ui.utils.GraphUtils;

public class AnswerMenuAction extends Action{
	private TeamMember poster;
    private PersistAndUpdate pu;
    private ForumThread thread;
    private Post post;
    private String author;
    
	public  AnswerMenuAction(ForumThread thread, TeamMember poster, PersistAndUpdate pu, Post post){
		this.poster = poster;
		this.pu = pu;
		this.post = post;
		
		this.thread = thread;
		setText("Set As Answer");
		
		LighthouseAuthor LHauthor = ModelUtility.getAuthor();
		
		String author = LHauthor.getName();
		
		if(!thread.getRootQuestion().getTeamMemberAuthor().getAuthor().getName().equals(author))
			this.setEnabled(false);
		
		if(thread.isClosed())
			this.setEnabled(false);
	}
	public void run() {
		LighthouseAuthor LHauthor = ModelUtility.getAuthor();
		post.setAnswer(true, new LHthreadCreator(LHauthor));
		pu.run();
	}
}
