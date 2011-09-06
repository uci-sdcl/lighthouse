package edu.uci.lighthouse.lighthouseqandathreads.view;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.LHforum;
import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class QuestionBoxView extends WindowFrame{
	final StyledText postNewThreadBox;
	private String message;
	private TeamMember tm;
	private LHforum forum;
	GridData compData;
	Composite composite;
	Button postButton;
	
	public QuestionBoxView(Composite parent, int style, LHforum forum, TeamMember tm) {
		super(parent, style);
		
		this.forum = forum;
		this.tm = tm;
		this.setLayout(new GridLayout(1, false));
		compData = new GridData(LayoutMetrics.QUESTION_BOX_VIEW_WIDTH,LayoutMetrics.QUESTION_BOX_VIEW_HEIGHT);

		
		
		composite = new Composite(this,SWT.None);
		composite.setLayout(new GridLayout(1,false));
		composite.setLayoutData(compData);
		composite.setBackground(ColorConstants.black);
		
		
		PostListener postListener = new PostListener();
		
		postButton = new Button(this, SWT.BORDER );
		postButton.setText("post");
		postButton.addSelectionListener(postListener);
		
		this.getElementMenu().addButton(postButton);
		
		
		GridData postNewThreadBoxData = new GridData(SWT.FILL,SWT.FILL,true,true);
		postNewThreadBox = new StyledText(composite, SWT.BORDER | SWT.V_SCROLL);

		postNewThreadBox.setLayoutData(postNewThreadBoxData);
		
		postNewThreadBox.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setMessage(postNewThreadBox.getText());

			}
		});
		
		postNewThreadBox.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				//change size of postNewThreadBox here?
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		

		

		
	}
	
	private class PostListener extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e) {
			
			
				Post newPost = new Post(true, "", message, tm);
				forum.addThread(newPost);
				postNewThreadBox.setText("");


			
		}
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}


}
