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

import org.eclipse.jface.action.Action;

import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.lighthouseqandathreads.PersistAndUpdate;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class CloseThreadMenuAction extends Action{
	private TeamMember tm;
    private PersistAndUpdate pu;
    private ForumThread thread;
    private String author;

	public  CloseThreadMenuAction(ForumThread thread, TeamMember tm, PersistAndUpdate pu){
		this.tm = tm;
		this.pu = pu;
		
		this.setThread(thread);
		setText("Close");
		
		LighthouseAuthor LHauthor = ModelUtility.getAuthor();
		
		String author = LHauthor.getName();
		
		if(!thread.getRootQuestion().getTeamMemberAuthor().getAuthor().getName().equals(author))
			this.setEnabled(false);
		
		if(thread.isClosed())
			this.setEnabled(false);
	}
	public void run() {
		getThread().setClosed(true);
		pu.run();

	}
	public void setThread(ForumThread thread) {
		this.thread = thread;
	}
	public ForumThread getThread() {
		return thread;
	}
}
