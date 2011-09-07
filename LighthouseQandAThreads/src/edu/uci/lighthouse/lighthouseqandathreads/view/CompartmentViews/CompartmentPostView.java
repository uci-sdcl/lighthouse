package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import edu.uci.lighthouse.lighthouseqandathreads.view.ForumElement;
import edu.uci.lighthouse.lighthouseqandathreads.view.LayoutMetrics;
import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class CompartmentPostView extends Composite{

	public CompartmentPostView(Composite parent, int style, Post post, TeamMember tm) {
		super(parent, style);
		
		this.setLayout(new GridLayout(1,false));
		Color postBack = new Color(this.getDisplay(), 255, 212, 102);

		Label replyLabel = new Label(this, SWT.None);
		
		
		parent.setBackground(postBack);
		Color labelBack = new Color(this.getDisplay(),255, 212, 102);
		replyLabel.setBackground(labelBack);
		this.setBackground(labelBack);
		TeamMember poster = post.getTeamMemberAuthor();
		replyLabel.setText(poster.getAuthor().getName()+": "+post.getMessage());
	}

}
