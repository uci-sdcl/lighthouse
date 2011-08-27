package edu.uci.lighthouse.lighthouseqandathreads.view;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class ConversationList extends ScrolledComposite{

	Composite composite;
	
	public ConversationList(Composite parent, int style) {
		super(parent, style);
	      this.setBackground(ColorConstants.blue);
	      
			GridData compsiteData = new GridData(580, 450);
			this.setLayout(new GridLayout(1, false));
			this.setLayoutData(compsiteData);
			
			composite = new Composite(this, SWT.None);
			this.setContent(composite);
			
			composite.setLayout(new GridLayout(1,false));
			this.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public void addConversationElement(ForumThread thread, TeamMember tm){
		ThreadView threadView = new ThreadView(composite, SWT.None,thread, tm);
		composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		composite.layout();
	}
	
	public void addConversationElement(ConversationElement element){
		element.setParent(composite);
		composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		composite.layout();
	}

}
