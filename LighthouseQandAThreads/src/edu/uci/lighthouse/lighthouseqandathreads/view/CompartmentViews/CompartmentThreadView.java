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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Panel;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.zest.core.viewers.GraphViewer;

import edu.uci.lighthouse.core.controller.Controller;
import edu.uci.lighthouse.core.data.ISubscriber;
import edu.uci.lighthouse.lighthouseqandathreads.ForumController;
import edu.uci.lighthouse.lighthouseqandathreads.PersistAndUpdate;
import edu.uci.lighthouse.lighthouseqandathreads.PostController;
import edu.uci.lighthouse.lighthouseqandathreads.actions.AnswerMenuAction;
import edu.uci.lighthouse.lighthouseqandathreads.actions.CloseThreadMenuAction;
import edu.uci.lighthouse.lighthouseqandathreads.actions.PinMenuAction;
import edu.uci.lighthouse.lighthouseqandathreads.actions.QAcontextMenuAction;
import edu.uci.lighthouse.lighthouseqandathreads.actions.ReplyMenuAction;
import edu.uci.lighthouse.lighthouseqandathreads.view.ForumElement;
import edu.uci.lighthouse.lighthouseqandathreads.view.ForumView;
import edu.uci.lighthouse.lighthouseqandathreads.view.LayoutMetrics;
import edu.uci.lighthouse.lighthouseqandathreads.view.ListComposite;
import edu.uci.lighthouse.lighthouseqandathreads.view.RespondBoxView;

import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.LHforum;
import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;
import edu.uci.lighthouse.ui.utils.GraphUtils;
import edu.uci.lighthouse.ui.views.EmergingDesignView;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class CompartmentThreadView extends Composite implements ISubscriber {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6013962858601965104L;

	// final StyledText postNewThreadBox;
	private String reply = "";
	private Composite replyComposite;
	private ForumThread thread;
	private TeamMember tm;
	private PersistAndUpdate pu;
	private ListComposite listOfReplies;
	private ScrolledComposite scroller;
	private boolean pin = false;
	private ReplyMenuAction rmAction;
	private CloseThreadMenuAction ctma;
	
	public CompartmentThreadView(Composite parent, int style,
			final ForumThread thread, final TeamMember tm,
			final PersistAndUpdate pu) {
		super(parent, style);
		GridLayout layout = new GridLayout(1, false);
		layout.horizontalSpacing = 1;
		layout.verticalSpacing = 1;
		layout.marginWidth = 1;
		layout.marginHeight = 1;
		
		this.setLayout(layout);
		Color threadBack2 = new Color(this.getDisplay(), 231, 232, 130);

		this.setBackground(threadBack2);
		this.thread = thread;
		this.tm = tm;
		this.pu = pu;

		scroller = new ScrolledComposite(this, SWT.V_SCROLL | SWT.H_SCROLL);
		GridData scrollData = new GridData(
				LayoutMetrics.CONVERSATION_LIST_WIDTH,
				LayoutMetrics.CONVERSATION_LIST_HEIGHT);
		scrollData.horizontalAlignment = GridData.CENTER;
		scroller.setLayoutData(scrollData);
		scroller.setLayout(layout);

		listOfReplies = new ListComposite(scroller, SWT.None);
		scroller.setContent(listOfReplies);
		scroller.setMinSize(listOfReplies.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		Color postBack = new Color(this.getDisplay(), 255, 212, 102);
		Color scrollerBack = new Color(this.getDisplay(), 231,232,130);
		scroller.setBackground(scrollerBack);

		// root question

		TeamMember poster = thread.getRootQuestion().getTeamMemberAuthor();
		
		GridData postViewData = new GridData(SWT.FILL,SWT.FILL,true,true);

		CompartmentPostView cpv = new CompartmentPostView(this, SWT.None,
				thread.getRootQuestion(), poster, false, thread, pu);
		

		listOfReplies.add(cpv);
		listOfReplies.setSize(listOfReplies.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

		listOfReplies.renderList();

		// ---add reply posts
		for (Post post : thread.getRootQuestion().orderResponses()) {

			poster = post.getTeamMemberAuthor();

			CompartmentPostView cpv2 = new CompartmentPostView(this, SWT.None,
					post, poster, true, thread, pu);
			

			

			listOfReplies.add(cpv2);
			listOfReplies.setSize(listOfReplies.computeSize(SWT.DEFAULT,
					SWT.DEFAULT));

			listOfReplies.renderList();
		}

	//	MenuManager menuMgr = new MenuManager("Thread");
	//	menuMgr.addMenuListener(new IMenuListener() {
	//		@Override
	//		public void menuAboutToShow(IMenuManager manager) {
//
	//			
//
	//		}
	//	});
		
		
		
	//	menuMgr.setRemoveAllWhenShown(true);

	//	Menu menuBar = new Menu(this.getShell(), SWT.BAR);
	//	menuMgr.fill(menuBar, -1);
		
		ToolBarManager tbm = new ToolBarManager();
		PinMenuAction pma = new PinMenuAction(CompartmentThreadView.this);
		
		rmAction = new ReplyMenuAction(thread, tm, pu, this,pma);
		tbm.add(rmAction);

		ctma = new CloseThreadMenuAction(thread,
				tm, pu);
		tbm.add(ctma);
		
		tbm.add(new Separator());
		
		
		tbm.add(pma);
		
	
		ToolBar bar = tbm.createControl(this);
		
		
	
		GridData barData = new GridData(SWT.FILL,SWT.FILL,true,true);
		bar.setLayoutData(barData);
		
	//	this.getShell().setMenuBar(menuBar);

	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getReply() {
		return reply;
	}

	public void updateView(LHforum forum) {
		
		ForumThread updatedThread = forum.getThreadWithRootQuestion(thread.getRootQuestion());
		thread = updatedThread;
		rmAction.setThread(updatedThread);
		ctma.setThread(updatedThread);
		
		if(updatedThread == null)
			return;
		
		listOfReplies.clearChildren();
		TeamMember poster = updatedThread.getRootQuestion().getTeamMemberAuthor();

		CompartmentPostView cpv = new CompartmentPostView(this, SWT.None,
				updatedThread.getRootQuestion(), poster, false, updatedThread, pu);

		listOfReplies.add(cpv);
		listOfReplies.setSize(listOfReplies.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

		listOfReplies.renderList();

		// ---add reply posts
		for (Post post : updatedThread.getRootQuestion().orderResponses()) {

			poster = post.getTeamMemberAuthor();
			CompartmentPostView cpv2 = new CompartmentPostView(this, SWT.None,
					post, poster, true, updatedThread, pu);

			listOfReplies.add(cpv2);
			listOfReplies.setSize(listOfReplies.computeSize(SWT.DEFAULT,
					SWT.DEFAULT));

			listOfReplies.renderList();
		}

		this.layout();
	}

	@Override
	public void receive(List<LighthouseEvent> events) {
		//updateView();

	}

	public void flipPin() {
		pin = !pin;
		
	}

	public void setPin(boolean pin) {
		this.pin = pin;
	}

	public boolean isPin() {
		return pin;
	}

}
