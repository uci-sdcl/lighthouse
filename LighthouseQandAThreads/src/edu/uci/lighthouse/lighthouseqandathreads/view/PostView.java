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

import java.util.Observer;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import edu.uci.lighthouse.lighthouseqandathreads.Activator;
import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class PostView extends ForumElement {

	Post post;
	private TeamMember tm;
	MenuItem answerItem;
	Menu popUp;

	public PostView(Composite parent, int style, Post post, TeamMember tm) {
		super(parent, style);
		this.post = post;
		RowData compsiteData = new RowData(LayoutMetrics.POST_VIEW_WIDTH,
				LayoutMetrics.POST_VIEW_HEIGHT);
		this.tm = tm;
		this.setLayout(new GridLayout(2, false));
		this.setLayoutData(compsiteData);
		this.setBackground(ColorConstants.white);

		Color color;

		color = new Color(this.getDisplay(), 255, 212, 102);

		Color authorColor = new Color(this.getDisplay(), 33, 138, 255);

		this.setBackground(color);
		Label authorLabel = new Label(this, SWT.None);
		authorLabel.setText(post.getTeamMemberAuthor().getAuthor().getName());
		authorLabel.setForeground(authorColor);
		authorLabel.setBackground(color);

		Label label = new Label(this, SWT.None);
		label.setText(post.getMessage());
		label.setBackground(color);
		label.setForeground(ColorConstants.black);

		

		popUp = new Menu(this.getShell(), SWT.POP_UP);
		this.setMenu(popUp);
		answerItem = new MenuItem(popUp, SWT.PUSH);
		answerItem.setText("set as answer");
		answerItem.addSelectionListener(new AnswerSelectionListener());
		popUp.addMenuListener(new PostMenuListener());

	}

	public Post getPost() {
		return post;
	}

	private class AnswerSelectionListener implements SelectionListener {
		public void widgetSelected(SelectionEvent e) {
			post.setAnswer(true, tm);
		}

		public void widgetDefaultSelected(SelectionEvent e) {
		}
	}

	private class PostMenuListener implements MenuListener {
		public void menuHidden(MenuEvent e) {
		}

		public void menuShown(MenuEvent e) {
			answerItem.setEnabled(post.canSetAsAnswer(tm));
		}

	}

}
