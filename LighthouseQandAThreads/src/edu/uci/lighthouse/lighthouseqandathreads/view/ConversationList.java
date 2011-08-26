package edu.uci.lighthouse.lighthouseqandathreads.view;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import edu.uci.lighthouse.model.QAforums.ForumThread;

public class ConversationList extends Composite{

	public ConversationList(Composite parent, int style) {
		super(parent, style);
	      this.setBackground(ColorConstants.blue);
	      
	      GridData compsiteData = new GridData(560, 500);

			this.setLayout(new GridLayout(1, false));
			this.setLayoutData(compsiteData);
	}
	
	public void addConversationElement(ForumThread thread){
		ThreadView threadView = new ThreadView(this, SWT.None,thread);
		Animation.markBegin();
		this.layout();
		Animation.run(300);
	}
	
	public void addConversationElement(ConversationElement element){
		element.setParent(this);
	}

}
