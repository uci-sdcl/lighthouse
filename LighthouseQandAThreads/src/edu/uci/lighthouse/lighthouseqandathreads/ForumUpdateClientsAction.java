package edu.uci.lighthouse.lighthouseqandathreads;

import java.util.ArrayList;

import edu.uci.lighthouse.core.dbactions.IDatabaseAction;
import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.LHEntityDAO;
import edu.uci.lighthouse.model.jpa.LHEventDAO;

public class ForumUpdateClientsAction implements IDatabaseAction{

	private LighthouseEntity entity;
	
	public ForumUpdateClientsAction(LighthouseEntity entity){
		this.entity = entity;
	}
	
	public void run() throws JPAException {
		LighthouseAuthor author = ModelUtility.getAuthor();
		LighthouseEvent lh = new LighthouseEvent(LighthouseEvent.TYPE.MODIFY,author,entity);
		ArrayList<LighthouseEvent> listOfEvents = new ArrayList<LighthouseEvent>();
		listOfEvents.add(lh);
		
		LHEntityDAO entityDAO = new LHEntityDAO();
		LHEventDAO dao = new LHEventDAO();
		try {
			entityDAO.save(entity);
			dao.saveListEvents(listOfEvents,null);
		} catch (JPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
