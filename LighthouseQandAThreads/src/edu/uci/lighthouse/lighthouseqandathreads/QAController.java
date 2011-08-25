package edu.uci.lighthouse.lighthouseqandathreads;

import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

import edu.uci.lighthouse.core.controller.Controller;
import edu.uci.lighthouse.core.dbactions.pull.FetchNewEventsAction;
import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.QAforums.Init;
import edu.uci.lighthouse.model.QAforums.LHforum;
import edu.uci.lighthouse.model.QAforums.QAlighthouseEvent;
import edu.uci.lighthouse.model.QAforums.Update;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.LHEntityDAO;
import edu.uci.lighthouse.ui.utils.GraphUtils;

public class QAController implements Observer {
	NewQuestionDialog nqDialog;
	LHforum forum;
	LighthouseEntity entity;

	public QAController(NewQuestionDialog dialog, LHforum forum,
			LighthouseEntity entity) {
		nqDialog = dialog;
		nqDialog.getObservablePoint().addObserver(this);

		this.forum = forum;
		//need to init all the model's self observers because Observers are destroyed once model is marshaled and unmarshaled. 
		this.forum.initObserving();
		this.forum.addObserver(this);
		this.entity = entity;

	}

	public void populateTree(LHforum forum) {
		nqDialog.populateTree(forum);
	}

	public void stopObserving() {
		forum.deleteObserver(this);
	}

	@Override
	public void update(Observable arg0, Object arg1) {

		if (arg0 == forum && arg1 instanceof Update) {
			populateTree(forum);
			
			Controller.getInstance().getBuffer().offer(new ForumUpdateClientsAction(entity));

			LighthouseAuthor author = ModelUtility.getAuthor();
			QAlighthouseEvent lh = new QAlighthouseEvent(LighthouseEvent.TYPE.MODIFY,author,entity);
			//lh.setTimestamp(new Date(0));
			ArrayList<LighthouseEvent> listOfEvents = new ArrayList<LighthouseEvent>();
			listOfEvents.add(lh);
			
			Controller.getInstance().getBuffer().offer(new ForumAddEventAction(listOfEvents));
			
			
			//refresh locally
			GraphUtils.rebuildFigureForEntity(entity);
			
		} else if (arg0 == nqDialog.getObservablePoint()
				&& arg1 instanceof Init) {
			populateTree(forum);
		}

	}

}
