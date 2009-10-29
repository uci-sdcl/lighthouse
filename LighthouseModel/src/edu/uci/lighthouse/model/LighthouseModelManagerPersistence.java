package edu.uci.lighthouse.model;

import java.util.Date;

import edu.uci.lighthouse.model.jpa.JPAUtilityException;
import edu.uci.lighthouse.model.jpa.LHEventController;

public class LighthouseModelManagerPersistence extends LighthouseModelManager {

	public LighthouseModelManagerPersistence(LighthouseModel model) {
		super(model);
	}

	@Override
	public void addEvent(LighthouseEvent event) throws JPAUtilityException {
		super.addEvent(event);
		saveEventIntoDatabase(event);
	}

	public void saveAllIntoDataBase() throws JPAUtilityException {
		for (LighthouseEvent event : this.model.getListEvents()) {
			saveEventIntoDatabase(event);
		}
	}
	
	private void saveEventIntoDatabase(LighthouseEvent event) throws JPAUtilityException {
		event.setTimestamp(new Date()); // FIXME get time from database
		LHEventController.getInstance().save(event);
	}
	
}
