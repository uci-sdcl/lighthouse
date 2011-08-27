package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class PostView extends ConversationElement{

	Post post;
	private TeamMember tm;
	private ObservablePoint observablePoint = new ObservablePoint();

	public PostView(Composite parent, int style, Post post, TeamMember tm) {
		super(parent, style);
		 this.post = post;
	      GridData compsiteData = new GridData(580, 40);
	      this.tm = tm;
			this.setLayout(new GridLayout(1, false));
			this.setLayoutData(compsiteData);
			
		Label label = new Label(this,SWT.None);
		label.setText(tm.getAuthor().getName()+" - "+post.getMessage());
		this.addMouseListener(new Listener());
	}
	

	
	private class Listener implements MouseListener{
		public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {		
		}
		public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
			observablePoint.changed(post);
		}
		public void mouseUp(org.eclipse.swt.events.MouseEvent e) {			
		}
	}



}
