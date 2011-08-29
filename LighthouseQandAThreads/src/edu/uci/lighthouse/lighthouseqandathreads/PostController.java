package edu.uci.lighthouse.lighthouseqandathreads;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import edu.uci.lighthouse.lighthouseqandathreads.view.ForumElement;
import edu.uci.lighthouse.lighthouseqandathreads.view.ThreadView;
import edu.uci.lighthouse.model.QAforums.AddEvent;
import edu.uci.lighthouse.model.QAforums.Post;


public class PostController implements IController<Post> {
	private Post post;
	private ForumElement view;
	
	public PostController(Post post, ForumElement view){
		this.post = post;
		this.view = view;
		post.addObserver(this);
		view.observeMe(this);
	}
	
	public void update(Observable o, Object arg) {
		
		if(o instanceof Post && arg instanceof AddEvent){
			AddEvent<Post,Post> event = (AddEvent<Post,Post>)arg;
			//really need a better way of updating view...
			ThreadView threadView = (ThreadView)view.getParent().getParent();
			threadView.addPost(event.getAddition(), event.getAddition().isRoot()); 
		}

	}


	public Composite getView() {
		return view;
	}


	public Post getModel() {
		return post;
	}


	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
