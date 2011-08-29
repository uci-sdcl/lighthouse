package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.Observer;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class PostView extends ForumElement{

	Post post;
	private TeamMember tm;

	public PostView(Composite parent, int style, Post post, TeamMember tm) {
		super(parent, style);
		 this.post = post;
	      RowData compsiteData = new RowData(LayoutMetrics.POST_VIEW_WIDTH, LayoutMetrics.POST_VIEW_HEIGHT);
	      this.tm = tm;
			this.setLayout(new GridLayout(2, false));
			this.setLayoutData(compsiteData);
			this.setBackground(ColorConstants.white);
			
		
		Label authorLabel = new Label(this,SWT.None);
		authorLabel.setText(tm.getAuthor().getName());
		authorLabel.setBackground(ColorConstants.lightGray);
		
		Label label = new Label(this,SWT.None);
		label.setText(post.getMessage());
		label.setBackground(ColorConstants.white);
		
		
		Listener listener = new Listener();
		this.addMouseListener(listener);
		label.addMouseListener(listener);
		
		
	}
	
	public Post getPost(){
		return post;
	}

	
	private class Listener implements MouseListener{
		public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {		
		}
		public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
			
		}
		public void mouseUp(org.eclipse.swt.events.MouseEvent e) {			
		}
	}



}
