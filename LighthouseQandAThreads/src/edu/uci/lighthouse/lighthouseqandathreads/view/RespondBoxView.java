package edu.uci.lighthouse.lighthouseqandathreads.view;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class RespondBoxView extends ConversationElement {

	public RespondBoxView(Composite parent, int style) {
		super(parent, style);
	      GridData compsiteData = new GridData(500, 40);

			this.setLayout(new GridLayout(1, false));
			this.setLayoutData(compsiteData);
			this.setBackground(ColorConstants.darkGray);
	}

}
