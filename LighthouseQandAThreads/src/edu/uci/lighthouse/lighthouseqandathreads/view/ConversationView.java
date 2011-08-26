package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.ArrayList;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.Post;

public class ConversationView extends Composite{

	ArrayList<ConversationElement> elements = new ArrayList<ConversationElement>();
	private String message;
	private ConversationList cl;
	
	public ConversationView(Composite parent, int style) {
		super(parent, style);
		
		//layout
		GridData compsiteData = new GridData(510, 600);
		this.setLayout(new GridLayout(1, false));
		this.setLayoutData(compsiteData);
		this.setBackground(ColorConstants.red);
		
		//post new thread box-----------------------------
		GridData postBoxCompoiteData = new GridData(430, 40);
		
		Composite postBoxCompoite = new Composite(this, SWT.NONE);
		postBoxCompoite.setLayout(new GridLayout(2, false));
		postBoxCompoite.setLayoutData(postBoxCompoiteData);
		
		GridData postNewThreadBoxData = new GridData(350, 30);
		final StyledText postNewThreadBox = new StyledText(postBoxCompoite, SWT.BORDER);
		postNewThreadBox.setLayoutData(postNewThreadBoxData);
		
		postNewThreadBox.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setMessage(postNewThreadBox.getText());

			}
		});
		
		Button postButton = new Button(postBoxCompoite, SWT.BORDER);
		postButton.setText("post");
		postButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				/*Post newPost = new Post(true, replySubject, reply, tm);
				Post replyeePost = getSelectedPost();
				
				if(replyeePost != null){
					replyeePost.addResponse(newPost);
					replyBox.setText("");
					stSubject.setText("");
				}*/
			}
		});
		//---------------------------------------------------
		
		  GridData scData = new GridData(400, 500);
	      ScrolledComposite sc = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
	      sc.setLayoutData(scData);
	      cl = new ConversationList(sc, SWT.NONE);
	      sc.setContent(cl);


		
		
	}
	
	public void addConversationElement(ForumThread thread){
		cl.addConversationElement(thread);
	}
	
	public void addConversationElement(ConversationElement element){
		cl.addConversationElement(element);
	}
	
	public void expand(){}
	
	public void collapse(){}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
