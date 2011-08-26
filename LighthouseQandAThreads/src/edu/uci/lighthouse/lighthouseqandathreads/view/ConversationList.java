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

public class ConversationList extends Composite{


	public ConversationList(Composite parent, int style) {
		super(parent, style);
	      
	      
	        GridData compsiteData = new GridData(400, 500);
			this.setLayout(new GridLayout(1, false));
			this.setLayoutData(compsiteData);
			

			this.setBackground(ColorConstants.lightBlue);

		      
		    /*  this.setExpandHorizontal(true);
		      this.setExpandVertical(true);
		      this.setContent(composite);
		      this.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		      this.setAlwaysShowScrollBars(true);*/

	}
	
	public void addConversationElement(ForumThread thread){
		ThreadView threadView = new ThreadView(this, SWT.None,thread);
		Animation.markBegin();
		this.layout();
		Animation.run(150);
	}
	
	public void addConversationElement(ConversationElement element){
		element.setParent(this);
	}

}
