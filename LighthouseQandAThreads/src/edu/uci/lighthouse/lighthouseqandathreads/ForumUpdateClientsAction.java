package edu.uci.lighthouse.lighthouseqandathreads;

import java.util.ArrayList;
import java.util.Date;

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
		LHEntityDAO entityDAO = new LHEntityDAO();
		try {
			entityDAO.save(entity);
		} catch (JPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
