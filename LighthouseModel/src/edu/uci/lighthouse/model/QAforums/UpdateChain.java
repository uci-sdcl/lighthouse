package edu.uci.lighthouse.model.QAforums;

import java.util.ArrayList;
import java.util.List;

public class UpdateChain implements IEvent{
	
	ArrayList<Update> updates = new ArrayList<Update>();
	
	public void addUpdate(Update update){
		updates.add(update);
	}
	
	public List<Update> getUpdates(){
		return updates;
	}

}
