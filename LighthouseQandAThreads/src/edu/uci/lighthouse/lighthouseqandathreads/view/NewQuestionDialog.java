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
package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.Collection;
import java.util.List;
import java.util.Observable;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

import edu.uci.lighthouse.lighthouseqandathreads.ForumController;
import edu.uci.lighthouse.lighthouseqandathreads.PersistAndUpdate;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.QAforums.IEvent;
import edu.uci.lighthouse.model.QAforums.Init;
import edu.uci.lighthouse.model.QAforums.LHforum;
import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;
import edu.uci.lighthouse.model.QAforums.ForumThread;

public class NewQuestionDialog extends MessageDialog {

	private String question;
	private String subject;
	private String reply;
	private String replySubject;
	private TeamMember tm;
	private LHforum forum;
	ForumController controller;

	public static int CLOSE = 0;
	private static String[] labelArray = { "Close" };
	private Tree tree;
	private StyledText messageBox;

	private ObservableClass observablePoint = new ObservableClass();

	private ForumView convoView;
	private LighthouseClass clazz;

	public NewQuestionDialog(Shell parentShell, String dialogTitle,
			Image dialogTitleImage, String dialogMessage, int dialogImageType,
			int defaultIndex, TeamMember tm, LHforum forum,
			LighthouseClass clazz) {

		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage,
				dialogImageType, labelArray, defaultIndex);

		this.tm = tm;
		this.forum = forum;
		this.clazz = clazz;

	}

	/**
	 * A ForumController is added to observe forum and the created ForumView
	 */
	public Control createCustomArea(Composite parent) {

		PersistAndUpdate pu = new PersistAndUpdate(clazz);
		
		convoView = new ForumView(parent, SWT.NULL, tm, forum,pu);
		
		
		controller = new ForumController(convoView, forum, clazz, tm, pu);
		controller.init();

		return convoView;
	}


	public void setQuestion(String question) {
		this.question = question;
	}

	public String getQuestion() {
		return question;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getReply() {
		return reply;
	}

	public void setReplySubject(String replySubject) {
		this.replySubject = replySubject;
	}

	public String getReplySubject() {
		return replySubject;
	}

	public ObservableClass getObservablePoint() {
		return observablePoint;
	}

	public class ObservableClass extends Observable {

		public void changed() {
			setChanged();
			notifyObservers();
			clearChanged();
		}

		public void changed(IEvent event) {
			setChanged();
			notifyObservers(event);
			clearChanged();
		}
	}

	public int open() {
		int response = super.open();
		return response;
	}
}
