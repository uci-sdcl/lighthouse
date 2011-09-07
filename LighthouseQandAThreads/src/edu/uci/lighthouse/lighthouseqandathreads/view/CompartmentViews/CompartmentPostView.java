package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import java.util.ArrayList;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import edu.uci.lighthouse.lighthouseqandathreads.view.ForumElement;
import edu.uci.lighthouse.lighthouseqandathreads.view.LayoutMetrics;
import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class CompartmentPostView extends Composite{

	public CompartmentPostView(Composite parent, int style, Post post, TeamMember tm, boolean leftSpacer) {
		super(parent, style);
	
		GridData data = new GridData(LayoutMetrics.POST_VIEW_WIDTH,
		LayoutMetrics.POST_VIEW_HEIGHT);
		this.setLayoutData(data);
		
		this.setLayout(new RowLayout());
		Color postBack = new Color(this.getDisplay(), 255, 212, 102);
		
		if(leftSpacer){
			addNewSpacer(this,false);
		}

		Label replyLabel = new Label(this, SWT.None);
		
		if(!leftSpacer){
			addNewSpacer(this,false);
		}
		
		
		this.setBackground(postBack);
		Color labelBack = new Color(this.getDisplay(),255, 212, 102);
		replyLabel.setBackground(labelBack);
		
		TeamMember poster = post.getTeamMemberAuthor();
		replyLabel.setText(poster.getAuthor().getName()+": "+post.getMessage());
		
	   

	    	
	    
	}
	
	public void setMenuItems(Menu menu){
    	MenuItem item = new MenuItem(this.getMenu(), SWT.None);
    	item.setText("set as answer");
    	
    	MenuItem item2 = new MenuItem(this.getMenu(), SWT.None);
    	item2.setText("close thread");
	}
	
	private void addNewSpacer(Composite composite, boolean check) {
		Composite spacer = new Composite(composite, SWT.None);
		spacer.setLayout(new RowLayout());
		spacer.setVisible(false);
		RowData rd = new RowData(20, LayoutMetrics.POST_VIEW_HEIGHT);
		spacer.setLayoutData(rd);

		
		if(check){
			spacer.setVisible(true);
			Label label = new Label(spacer,SWT.None);
			FontRegistry fr = new FontRegistry(this.getDisplay());
			Color backColor = new Color(this.getDisplay(), 231, 232, 130);
			spacer.setBackground(backColor);
			
			ArrayList<FontData> fdList = new ArrayList<FontData>();
			FontData fd = new FontData("Courier New",14,SWT.BOLD);
			fd.setHeight(20);
			fdList.add(fd);
			
			fr.put("checkFont",fdList.toArray(new FontData[0]));
			
			label.setFont(fr.get("checkFont"));
			label.setText("\u2713");
			label.setBackground(backColor);
			
		}
	}

}
