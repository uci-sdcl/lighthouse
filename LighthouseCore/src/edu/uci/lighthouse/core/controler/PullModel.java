package edu.uci.lighthouse.core.controler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.jpa.JPAUtilityException;
import edu.uci.lighthouse.model.jpa.LHEventController;

public class PullModel {

	private LighthouseModel model;
	private Date currentTime = new Date(); // FIXME remove value

	public PullModel(LighthouseModel lighthouseModel) {
		this.model = lighthouseModel;
		
		String date = 2009 + "/" + 9 + "/" + 29;
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
	    try {
			this.currentTime = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void run() throws JPAUtilityException {
		// TODO Start Thread
		
		List<LighthouseEvent> listEvents = timeoutProcedure();
		updateLighthouseModel(listEvents);
		
		// TODO Fire the Visualization
		
	}

	private void updateLighthouseModel(List<LighthouseEvent> listEvents) throws JPAUtilityException {
		for (LighthouseEvent event : listEvents) {
			new LighthouseModelManager(model).addEvent(event);
		}
	}

	private List<LighthouseEvent> timeoutProcedure() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("timestamp", currentTime);
		return LHEventController.getInstance().executeNamedQuery("LighthouseEvent.findByTimestamp", parameters);
	}

//	private void checkOutProcedure() {
//		
//	}
//
//	private void getAllEventsFromDatabase() {
//		List<LighthouseEvent> listEventDatabse = LHEventController.getInstance().list();
//	}

}
