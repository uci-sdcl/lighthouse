package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.Observer;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import edu.uci.lighthouse.lighthouseqandathreads.Activator;
import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class PostView extends ForumElement{

	Post post;
	private TeamMember tm;
	MenuItem answerItem;
	Menu popUp;
	
	public PostView(Composite parent, int style, Post post, TeamMember tm) {
		super(parent, style);
		 this.post = post;
	      RowData compsiteData = new RowData(LayoutMetrics.POST_VIEW_WIDTH, LayoutMetrics.POST_VIEW_HEIGHT);
	      this.tm = tm;
			this.setLayout(new GridLayout(2, false));
			this.setLayoutData(compsiteData);
			this.setBackground(ColorConstants.white);
			
			Color color = new Color(this.getDisplay(), 255,212, 102);
			
			Color authorColor = new Color(this.getDisplay(),33,138,255);
			
		this.setBackground(color);
		Label authorLabel = new Label(this,SWT.None);
		authorLabel.setText(tm.getAuthor().getName());
		authorLabel.setForeground(authorColor);
		authorLabel.setBackground(color);
		
		Label label = new Label(this,SWT.None);
		label.setText(post.getMessage());
		label.setBackground(color);
		label.setForeground(ColorConstants.black);
		
		Listener listener = new Listener();
		this.addMouseListener(listener);
		label.addMouseListener(listener);
		
		popUp = new Menu(this.getShell(),SWT.POP_UP);
		this.setMenu(popUp);
		answerItem = new MenuItem(popUp, SWT.PUSH);
		answerItem.setText("set as answer");
		answerItem.addSelectionListener(new AnswerSelectionListener());
		
		
	}
	
	public Post getPost(){
		return post;
	}

	private class AnswerSelectionListener implements SelectionListener{

		@Override
		public void widgetSelected(SelectionEvent e) {
			System.out.println("Answer Chosen");
			
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		}

		
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
