package edu.uci.lighthouse.core.dbactions.pull;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.uci.lighthouse.core.controller.PullModel;
import edu.uci.lighthouse.core.controller.UpdateLighthouseModel;
import edu.uci.lighthouse.core.data.ISubscriber;
import edu.uci.lighthouse.core.dbactions.IDatabaseAction;
import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.QAforums.LHforum;
import edu.uci.lighthouse.model.jpa.JPAException;

public class FetchNewEventsAction implements IDatabaseAction {
	
	private static Logger logger = Logger.getLogger(FetchNewEventsAction.class);
	
	private static final long serialVersionUID = 7200580843968876241L;
	
	private ArrayList<ISubscriber> listOfSubs = new ArrayList<ISubscriber>();
	
	public FetchNewEventsAction(){}
	
	public FetchNewEventsAction(List<ISubscriber> subs){
		listOfSubs.clear();
		listOfSubs.addAll(subs);
	}
	
	private void sendSubscribersEvents(List<LighthouseEvent> events){
		System.out.println(listOfSubs.size());
		for(ISubscriber sub: listOfSubs){
			System.out.println("sending events to subscribers");
			sub.receive(events);
		}
	}

	@Override
	public void run() throws JPAException {
		LighthouseAuthor author = ModelUtility.getAuthor();
		PullModel pullModel = PullModel.getInstance();
		List<LighthouseEvent> events = pullModel.getNewEventsFromDB(author);
		System.out.println("pulled events "+events.size()+" after "+pullModel.getTimeStamp());

		if (events.size()>0) {
			//
			sendSubscribersEvents(events);
			UpdateLighthouseModel.addEvents(events);
			ModelUtility.fireModificationsToUI(events);
			
		} else {
			logger.debug("Events fecthed from database: " + events);
		}
	}

}
