package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.MouseEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;

import edu.uci.lighthouse.lighthouseqandathreads.PostController;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class ThreadView extends WindowFrame implements IHasObservablePoint
		 {

	private ForumThread thread;
	private TeamMember tm;
	private Button replyButton;
	
	public ThreadView(Composite parent, int style, ForumThread thread,
			TeamMember tm) {
		super(parent, style);
		this.thread = thread;
		this.tm = tm;

		this.setLayout(new GridLayout(1, false));
		
		Color backColor = new Color(this.getDisplay(),231,232,130);
		
		this.setBackground(backColor);

		addPost(thread.getRootQuestion(), true);
		
		 replyButton = new Button(this,SWT.None);
		 replyButton.setText("reply");
		 replyButton.addSelectionListener(new MenuListener());
		 
			Label statsLabel = new Label(this, SWT.None);
			statsLabel.setText("responses: "+thread.getRootQuestion().getResponses().size());
			statsLabel.setBackground(backColor);
			getElementMenu().addLabel(statsLabel);
		
		getElementMenu().addButton(replyButton);
		Color menuColor = new Color(this.getDisplay(),231,232,130);
		getElementMenu().setBackground(backColor);
		

	}

	public ForumThread getThread() {
		return thread;
	}

	public void addPost(Post post, boolean root) {

		addPostView(post, tm, root);
		addResponsePosts(post.getResponses());
	}

	public void addResponsePosts(Set<Post> posts) {

		for (Post post : posts) {

			addPostView(post, tm, false);

			for (Post childPost : post.getResponses()) {
				addPost(childPost, false);
			}

		}

	}

	private void addNewSpacer(Composite composite) {
		Composite spacer = new Composite(composite, SWT.None);
		spacer.setVisible(false);
		RowData rd = new RowData(15, 10);
		spacer.setLayoutData(rd);
        Color backColor = new Color(this.getDisplay(),231,232,130);
        spacer.setBackground(backColor);
	}

	/**
	 * A PostController is added to observe post and its created PostView
	 * 
	 * @param post
	 * @param tm
	 * @param root
	 */
	private void addPostView(Post post, TeamMember tm, boolean root) {

		if (root) {
			Composite rowComposite = new Composite(this, SWT.NONE);
			rowComposite.setLayout(new RowLayout());
			rowComposite.setBackground(ColorConstants.white);
			PostView pv = new PostView(rowComposite, SWT.None, post, tm);
			PostController controller = new PostController(post, pv);
	        Color backColor = new Color(this.getDisplay(),231,232,130);
	        rowComposite.setBackground(backColor);
			addNewSpacer(rowComposite);
		} else {
			Composite rowComposite = new Composite(this, SWT.NONE);
			rowComposite.setLayout(new RowLayout());
			rowComposite.setBackground(ColorConstants.white);
			addNewSpacer(rowComposite);
			PostView pv = new PostView(rowComposite, SWT.None, post, tm);
			PostController controller = new PostController(post, pv);
	        Color backColor = new Color(this.getDisplay(),231,232,130);
	        rowComposite.setBackground(backColor);
		}

		this.setSize(this.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		this.layout();
	}


	private class MenuListener extends SelectionAdapter{
		public void widgetSelected(SelectionEvent e){
			if(e.getSource() == replyButton){
				
				ForumThread ft = getThread();
				
				if(getParent() instanceof ListComposite){
					ListComposite tl = (ListComposite)getParent();
					tl.disposeRespondBoxes();
					RespondBoxView box = new RespondBoxView(tl,
							SWT.None, ft.getRootQuestion(), ft.getRootQuestion().getTeamMemberAuthor());
					box.observeMe(tl);
					tl.addAfter(box,ThreadView.this);
					tl.setSize(tl.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					
					tl.renderList();
				}

			}
		}
	}
}
