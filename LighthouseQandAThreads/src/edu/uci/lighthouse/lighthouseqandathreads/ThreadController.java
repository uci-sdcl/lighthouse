package edu.uci.lighthouse.lighthouseqandathreads;

import java.util.Observable;

import org.eclipse.swt.widgets.Composite;

import edu.uci.lighthouse.lighthouseqandathreads.view.ThreadView;
import edu.uci.lighthouse.model.QAforums.ForumThread;

public class ThreadController implements IController<ForumThread>{
	
	private ForumThread thread;
	private ThreadView view;

	public ThreadController(ForumThread thread, ThreadView view){
		this.thread = thread;
		this.view = view;
		thread.addObserver(this);
		view.observeMe(this);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Composite getView() {
		// TODO Auto-generated method stub
		return view;
	}

	@Override
	public ForumThread getModel() {
		// TODO Auto-generated method stub
		return thread;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
