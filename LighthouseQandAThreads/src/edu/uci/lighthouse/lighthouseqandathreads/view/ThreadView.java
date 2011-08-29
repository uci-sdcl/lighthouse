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
import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.events.MouseListener;

import edu.uci.lighthouse.lighthouseqandathreads.PostController;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class ThreadView extends ConversationElement implements IHasObservablePoint, Observer{
	
	private ForumThread thread;
	private TeamMember tm;
	private int height = 30;
	private Composite spacer;
	
	
	public ThreadView(Composite parent, int style, ForumThread thread, TeamMember tm) {
		super(parent, style);
		this.thread = thread;
			this.tm = tm;

			this.setLayout(new GridLayout(1,false));
			this.setBackground(ColorConstants.white);
		
		
			addPost(thread.getRootQuestion(), true);
	
	}
	

	public ForumThread getThread(){
		return thread;
	}
	
	public void addPost(Post post, boolean root){
		
		addPostView(post,  tm, root);
		addResponsePosts(post.getResponses());
	}
	
	public void addResponsePosts(Set<Post> posts){

		
		for(Post post: posts){
			
			addPostView(post,  tm, false);
			
			for(Post childPost: post.getResponses()){
				addPost(childPost, false);
			}

		}


	}
	
	private void addNewSpacer(Composite composite){
		Composite spacer = new Composite(composite,SWT.None);
		spacer.setVisible(false);
		RowData rd = new RowData(15,10);
		spacer.setLayoutData(rd);
	
	}
	
	private void addPostView(Post post, TeamMember tm, boolean root){
		
		if(root){
			Composite rowComposite = new Composite(this, SWT.NONE);
			rowComposite.setLayout(new RowLayout());
			rowComposite.setBackground(ColorConstants.white);
			PostView pv = new PostView(rowComposite, SWT.None, post,tm);
			PostController controller = new PostController(post,pv);
			pv.observeMe(this);
			addNewSpacer(rowComposite);
		}else{
			Composite rowComposite = new Composite(this, SWT.NONE);
			rowComposite.setLayout(new RowLayout());
			rowComposite.setBackground(ColorConstants.white);
			addNewSpacer(rowComposite);
			PostView pv = new PostView(rowComposite, SWT.None, post,tm);
			PostController controller = new PostController(post,pv);
			pv.observeMe(this);
		}
		
		
		this.setSize(this.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		this.layout();
	}
	




	@Override
	public void update(Observable o, Object arg) {
		this.getObservablePoint().changed(this);
		
	}
	


}
