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
import org.eclipse.draw2d.Label;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.lighthouseqandathreads.PersistAndUpdate;
import edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews.CompartmentNewPostView;
import edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews.CompartmentReplyView;
import edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews.CompartmentThreadView;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.LHforum;
import edu.uci.lighthouse.model.QAforums.LHthreadCreator;
import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;
import edu.uci.lighthouse.ui.utils.GraphUtils;

public class ReplyMenuAction extends Action{
	private Shell postShell;
	private TeamMember tm;
    private PersistAndUpdate pu;
    private ForumThread thread;
    private CompartmentThreadView view;
    private PinMenuAction pma;
    private CompartmentReplyView npv;
   
	public  ReplyMenuAction(ForumThread thread, TeamMember tm, PersistAndUpdate pu, CompartmentThreadView view, PinMenuAction pma){

		this.tm = tm;
		this.pu = pu;
		
		this.setThread(thread);
		this.view = view;
		this.pma = pma;
		
		setText("Reply");
		
		if(thread.isClosed())
			this.setEnabled(false);
	}
	public void run() {
		
		if(!view.isPin()){
			pma.run();
		}
		
		LighthouseAuthor author = ModelUtility.getAuthor();
		LHthreadCreator tm = new LHthreadCreator(author);

	
		GridLayout layout = new GridLayout(1, false);
		layout.horizontalSpacing = 1;
		layout.verticalSpacing = 1;
		layout.marginWidth = 1;
		layout.marginHeight = 1;
		
		postShell = new Shell(GraphUtils.getGraphViewer()
				.getGraphControl().getDisplay().getActiveShell());
		
		postShell.setLayout(layout);
		
		
		npv = new CompartmentReplyView(postShell, SWT.None,getThread(),tm,pu);
		
		
		postShell.setBackground(ColorConstants.black);
		
		postShell.setSize(postShell.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		postShell.setText("Reply");
		
		postShell.open();
		
		
		

	}
	public void setThread(ForumThread thread) {
		this.thread = thread;
		if(npv != null)
			npv.setThread(thread);
		
	}
	public ForumThread getThread() {
		return thread;
	}
	

	


}
