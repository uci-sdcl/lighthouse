package edu.uci.lighthouse.lighthouseqandathreads.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class PostView extends Composite{

	Post post;
	private TeamMember tm;

	public PostView(Composite parent, int style, Post post, TeamMember tm) {
		super(parent, style);
		 this.post = post;
	      GridData compsiteData = new GridData(580, 40);
	      this.tm = tm;
			this.setLayout(new GridLayout(1, false));
			this.setLayoutData(compsiteData);
			
		Label label = new Label(this,SWT.None);
		label.setText(post.getMessage());
		this.addMouseListener(new Listener());
	}
	
	private class Listener implements MouseListener{


		@Override
		public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
			RespondBoxView box = new RespondBoxView(getParent().getParent(),SWT.None, post,tm);
			getParent().getParent().layout();
			box.moveBelow(PostView.this);
			getParent().getParent().layout();
			
		}

		@Override
		public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}

}
