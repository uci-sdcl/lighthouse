package edu.uci.lighthouse.lighthouseqandathreads;

import java.util.ArrayList;
import java.util.Date;

import edu.uci.lighthouse.core.controller.Controller;
import edu.uci.lighthouse.core.controller.UpdateLighthouseModel;
import edu.uci.lighthouse.core.dbactions.push.FileEventAction;
import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.ui.utils.GraphUtils;

public class PersistAndUpdate implements IPersistAndUpdate{

	private LighthouseEntity entity;
	
	public PersistAndUpdate(LighthouseEntity entity){
		this.entity = entity;
	}
	
	
	public void run() {
		Controller.getInstance().getBuffer()
				.offer(new ForumUpdateClientsAction(getEntity()));

		LighthouseAuthor author = ModelUtility.getAuthor();
		LighthouseEvent lh = new LighthouseEvent(LighthouseEvent.TYPE.MODIFY,
				author, getEntity());
		//LighthouseEvent foundEvent = LighthouseModel.getInstance().getEventByID(lh.getId());
	
		//if(foundEvent != null){
	//	lh = foundEvent;
		//	System.out.println("[found event in model]");
	//	}else{
	//		System.out.println("[NOT found event in model]");
//
//		}
		
		
		//lh.setTimestamp(new Date());

		// lh.setTimestamp(new Date(0));
		ArrayList<LighthouseEvent> listOfEvents = new ArrayList<LighthouseEvent>();
		listOfEvents.add(lh);

		//Controller.getInstance().getBuffer()
		//		.offer(new ForumAddEventAction(listOfEvents));
		//UpdateLighthouseModel.addEvents(listOfEvents);
		
		
		UpdateLighthouseModel.addEvents(listOfEvents);
		
		//LighthouseModelManager  mm = new LighthouseModelManager(LighthouseModel.getInstance());
		//mm.addEvent(lh);
		
		ModelUtility.fireModificationsToUI(listOfEvents);
		
		Controller.getInstance().getBuffer()
				.offer(new ForumAddEventAction(listOfEvents));
		
		//FileEventAction fileEventAction = new FileEventAction(listOfEvents);
		//Controller.getInstance().getBuffer().offer(fileEventAction);
		
	//	LighthouseModelManager manager = new LighthouseModelManager(LighthouseModel.getInstance());
	//	manager.addEvent(lh);
		
	//	LighthouseModel.getInstance().fireModelChanged();
		
		// refresh locally
		GraphUtils.rebuildFigureForEntity(getEntity());
	}



	public void setEntity(LighthouseEntity entity) {
		this.entity = entity;
	}



	public LighthouseEntity getEntity() {
		return entity;
	}
}
