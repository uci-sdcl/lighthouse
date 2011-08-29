package edu.uci.lighthouse.lighthouseqandathreads;

import java.util.Observable;

import org.eclipse.swt.widgets.Composite;

import edu.uci.lighthouse.lighthouseqandathreads.view.ConversationElement;
import edu.uci.lighthouse.model.QAforums.Post;


public class PostController implements IController<Post> {
	private Post post;
	private ConversationElement view;
	
	public PostController(Post post, ConversationElement view){
		this.post = post;
		this.view = view;
		post.addObserver(this);
		view.observeMe(this);
	}
	
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
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
