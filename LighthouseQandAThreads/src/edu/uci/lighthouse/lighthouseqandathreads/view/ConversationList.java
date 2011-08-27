package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class ConversationList extends ScrolledComposite implements Observer {

	ListComposite composite;

	public ConversationList(Composite parent, int style) {
		super(parent, style);
		this.setBackground(ColorConstants.blue);

		GridData compsiteData = new GridData(580, 450);
		this.setLayout(new GridLayout(1, false));
		this.setLayoutData(compsiteData);

		composite = new ListComposite(this, SWT.None);
		this.setContent(composite);

		composite.setLayout(new GridLayout(1, false));
		this.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	public void addConversationElement(ForumThread thread, TeamMember tm) {
		ThreadView threadView = new ThreadView(composite, SWT.None, thread, tm);
		threadView.observeMe(this);
		composite.add(threadView);
		composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		composite.renderList();
	}

	public void addConversationElement(ConversationElement element) {
		element.setParent(composite);
		element.observeMe(this);
		composite.add(element);
		composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		composite.renderList();
	}

	@Override
	public void update(Observable o, Object arg) {

		if (arg instanceof PostView) {
				
			Post post = ((PostView)arg).getPost();
			RespondBoxView box = new RespondBoxView(composite,
					SWT.None, post, post.getTeamMemberAuthor());
			composite.addAfter(box,(PostView)arg);
			composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			composite.renderList();
		}

	}

}
