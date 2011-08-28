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
	        
		      GridData compsiteData = new GridData(LayoutMetrics.RESPOND_BOX_VIEW_WIDTH, LayoutMetrics.RESPOND_BOX_VIEW_HEIGHT);
		      replyComposite.setLayout(new GridLayout(2, false));
		      replyComposite.setLayoutData(compsiteData);
		      replyComposite.setBackground(ColorConstants.darkGray);
			
			
			
			
			 closeButton = new Button(replyComposite,SWT.None);
			 closeButton.setText("x");
			
			getElementMenu().addButton(closeButton);
			
			GridData postNewThreadBoxData = new GridData(350, 100);
			postNewThreadBox = new StyledText(replyComposite, SWT.BORDER | SWT.V_SCROLL);
			postNewThreadBox.setLayoutData(postNewThreadBoxData);
			
			postNewThreadBox.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					setReply(postNewThreadBox.getText());

				}
			});
			
			Button postButton = new Button(replyComposite, SWT.BORDER);
			postButton.setText("reply");
			postButton.addSelectionListener(new ReplyListener());
			

	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getReply() {
		return reply;
	}
	
	private class MenuListener extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			if(e.getSource() == closeButton){
				RespondBoxView.this.dispose();
			}
		}
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
