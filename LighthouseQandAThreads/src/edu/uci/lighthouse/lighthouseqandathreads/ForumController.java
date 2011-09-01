package edu.uci.lighthouse.lighthouseqandathreads;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;

import edu.uci.lighthouse.core.controller.Controller;
import edu.uci.lighthouse.core.dbactions.pull.FetchNewEventsAction;
import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.lighthouseqandathreads.view.ForumView;
import edu.uci.lighthouse.lighthouseqandathreads.view.NewQuestionDialog;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.Init;
import edu.uci.lighthouse.model.QAforums.LHforum;
import edu.uci.lighthouse.model.QAforums.TeamMember;
import edu.uci.lighthouse.model.QAforums.AddEvent;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.LHEntityDAO;
import edu.uci.lighthouse.ui.utils.GraphUtils;

public class ForumController implements IController<LHforum> {
	ForumView view;
	LHforum forum;
	LighthouseEntity entity;
	HashMap<Object, Composite> modelToViewMap = new HashMap<Object, Composite>();
	TeamMember tm;
	private IPersistAndUpdate persisterAndUpdater;

	public ForumController(ForumView view, LHforum forum,
			LighthouseEntity entity, TeamMember tm) {
		this.view = view;

		this.forum = forum;

		
		this.forum.addObserver(this);
		this.entity = entity;
		this.tm = tm;

	}

	public void stopObserving() {
		forum.deleteObserver(this);
	}

	@Override
	public void update(Observable arg0, Object arg1) {

		if (arg0 == forum && arg1 instanceof AddEvent) {
			
			AddEvent update = (AddEvent) arg1;
			
			if (update.getAddition() instanceof ForumThread) {

				ForumThread thread = (ForumThread) update.getAddition();
				view.addConversationElement(thread, tm);

				//commits to database
				
			}

		}

	}

	public Composite getView() {
		return view;
	}

	public LHforum getModel() {
		return forum;
	}

	@Override
	public void init() {
		for (ForumThread thread : forum.getThreads()) {
			view.addConversationElement(thread, tm);
		}
	}

	@Override
	public IPersistAndUpdate getPersisterAndUpdater() {
		return persisterAndUpdater;
	}

	@Override
	public void setPersisterAndUpdater(IPersistAndUpdate persisterAndUpdater) {
		this.persisterAndUpdater = persisterAndUpdater;
	}

}
