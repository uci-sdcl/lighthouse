package edu.uci.lighthouse.lighthouseqandathreads.view;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class ConversationList extends Composite{

	public ConversationList(Composite parent, int style) {
		super(parent, style);
	      this.setBackground(ColorConstants.red);
	      
	      GridData compsiteData = new GridData(400, 500);

			this.setLayout(new GridLayout(1, false));
			this.setLayoutData(compsiteData);
	}
	
	public void addConversationElement(ConversationElement element){
		element.setParent(this);
	}

}
