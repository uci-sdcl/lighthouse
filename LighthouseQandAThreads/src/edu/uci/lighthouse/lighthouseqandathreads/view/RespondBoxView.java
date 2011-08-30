package edu.uci.lighthouse.lighthouseqandathreads.view;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class RespondBoxView extends WindowFrame {
	private String reply = "";
	final StyledText postNewThreadBox;
	private Post post;
	private TeamMember tm;

	private Composite replyComposite;
	private Button closeButton;

	public RespondBoxView(Composite parent, int style, Post post, TeamMember tm) {
		super(parent, style);

		replyComposite = new Composite(this, style);

		this.tm = tm;
		this.post = post;

		Color backColor = new Color(this.getDisplay(), 231, 232, 130);
		this.setBackground(backColor);
		getElementMenu().setBackground(backColor);

		GridData compsiteData = new GridData(
				LayoutMetrics.RESPOND_BOX_VIEW_WIDTH,
				LayoutMetrics.RESPOND_BOX_VIEW_HEIGHT);
		replyComposite.setLayout(new GridLayout(1, false));
		replyComposite.setLayoutData(compsiteData);
		Color replyBorderColor = new Color(this.getDisplay(), 33, 138, 255);
		replyComposite.setBackground(replyBorderColor);

		Button postButton = new Button(replyComposite, SWT.BORDER);
		postButton.setText("submit");
		postButton.addSelectionListener(new ReplyListener());
		getElementMenu().addButton(postButton);

		closeButton = new Button(replyComposite, SWT.None);
		closeButton.setText("x");
		closeButton.addSelectionListener(new MenuListener());

		getElementMenu().addButton(closeButton);

		GridData postNewThreadBoxData = new GridData(SWT.FILL, SWT.FILL, true,
				true);

		postNewThreadBox = new StyledText(replyComposite, SWT.BORDER
				| SWT.V_SCROLL);
		postNewThreadBox.setLayoutData(postNewThreadBoxData);
		Color color = new Color(this.getDisplay(), 255, 212, 102);
		postNewThreadBox.setBackground(color);

		postNewThreadBox.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setReply(postNewThreadBox.getText());

			}
		});

	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getReply() {
		return reply;
	}

	private class MenuListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			if (e.getSource() == closeButton) {
				RespondBoxView.this.getObservablePoint().changed(
						RespondBoxView.this);
			}
		}
	}

	private class ReplyListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			Post newPost = new Post(true, "", reply, tm);
			Post replyeePost = post;

			if (replyeePost != null) {
				replyeePost.addResponse(newPost);
				postNewThreadBox.setText("");
				RespondBoxView.this.getObservablePoint().changed(
						RespondBoxView.this);
			}
		}
	}

}
