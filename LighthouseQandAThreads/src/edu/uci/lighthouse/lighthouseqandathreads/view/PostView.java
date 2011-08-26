package edu.uci.lighthouse.lighthouseqandathreads.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import edu.uci.lighthouse.model.QAforums.Post;

public class PostView extends Composite{

	public PostView(Composite parent, int style, Post post) {
		super(parent, style);
		Label label = new Label(this,SWT.None);
		label.setText(post.getMessage());
	}

}
