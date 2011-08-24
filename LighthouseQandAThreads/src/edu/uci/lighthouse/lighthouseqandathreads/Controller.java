package edu.uci.lighthouse.lighthouseqandathreads;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.QAforums.Init;
import edu.uci.lighthouse.model.QAforums.LHforum;
import edu.uci.lighthouse.model.QAforums.Update;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.LHEntityDAO;

public class Controller implements Observer {
	NewQuestionDialog nqDialog;
	LHforum forum;
	LighthouseEntity entity;

	public Controller(NewQuestionDialog dialog, LHforum forum,
			LighthouseEntity entity) {
		nqDialog = dialog;
		nqDialog.getObservablePoint().addObserver(this);

		this.forum = forum;
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
			LHEntityDAO entityDAO = new LHEntityDAO();
			try {
				entityDAO.save(entity);
			} catch (JPAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (arg0 == nqDialog.getObservablePoint()
				&& arg1 instanceof Init) {
			populateTree(forum);
		}

	}

}
