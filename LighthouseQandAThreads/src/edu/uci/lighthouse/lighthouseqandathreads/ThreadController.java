package edu.uci.lighthouse.lighthouseqandathreads;

import java.util.Observable;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.widgets.Composite;

import edu.uci.lighthouse.lighthouseqandathreads.view.ThreadView;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.UpdateEvent;

public class ThreadController implements IController<ForumThread>{
	
	private ForumThread thread;
	private ThreadView view;
	private PersistAndUpdate persisterAndUpdater;

	public ThreadController(ForumThread thread, ThreadView view, PersistAndUpdate pu){
		this.thread = thread;
		this.view = view;
		thread.addObserver(this);
		view.observeMe(this);
		this.persisterAndUpdater = pu;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof ForumThread && arg instanceof UpdateEvent){
			view.setSolved();
			view.layout();
			//TODO: need to update so as to add check and update visual display on ThreadFigure
			
			//commits to database
			persisterAndUpdater.run();
		}
		
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

	@Override
	public PersistAndUpdate getPersisterAndUpdater() {
		return persisterAndUpdater;
	}

	@Override
	public void setPersisterAndUpdater(PersistAndUpdate persisterAndUpdater) {
		this.persisterAndUpdater = persisterAndUpdater;
	}

}
