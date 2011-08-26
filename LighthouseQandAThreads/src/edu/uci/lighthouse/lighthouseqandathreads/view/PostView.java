package edu.uci.lighthouse.lighthouseqandathreads.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import edu.uci.lighthouse.model.QAforums.Post;

public class PostView extends Composite{

	public PostView(Composite parent, int style, Post post) {
		super(parent, style);
	      GridData compsiteData = new GridData(400, 50);

			this.setLayout(new GridLayout(1, false));
			this.setLayoutData(compsiteData);
			
		Label label = new Label(this,SWT.None);
		label.setText(post.getMessage());
		
	}

}
