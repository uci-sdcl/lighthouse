package edu.uci.lighthouse.lighthouseqandathreads.view;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class RespondBoxView extends ConversationElement {
	private String reply = "";
	final StyledText postNewThreadBox;
	private Post post;
	private TeamMember tm;

	public RespondBoxView(Composite parent, int style, Post post, TeamMember tm) {
		super(parent, style);
	      GridData compsiteData = new GridData(500, 40);
	      this.tm = tm;
	        this.post = post;
			this.setLayout(new GridLayout(2, false));
			this.setLayoutData(compsiteData);
			this.setBackground(ColorConstants.darkGray);
			
			GridData postNewThreadBoxData = new GridData(350, 30);
			postNewThreadBox = new StyledText(this, SWT.BORDER);
			postNewThreadBox.setLayoutData(postNewThreadBoxData);
			
			postNewThreadBox.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					setReply(postNewThreadBox.getText());

				}
			});
			
			Button postButton = new Button(this, SWT.BORDER);
			postButton.setText("reply");
			postButton.addSelectionListener(new ReplyListener());
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getReply() {
		return reply;
	}
	
	private class ReplyListener extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e) {
			Post newPost = new Post(true, "", tm.getAuthor().getName()+" "+reply, tm);
			Post replyeePost = post;
			
			if(replyeePost != null){
				replyeePost.addResponse(newPost);
				postNewThreadBox.setText("");
				RespondBoxView.this.dispose();
			}
		}
	}

}
