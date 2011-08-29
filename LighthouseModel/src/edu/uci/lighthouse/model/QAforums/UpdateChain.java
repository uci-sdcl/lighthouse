package edu.uci.lighthouse.model.QAforums;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates order specifies the when the event happened
 * relative to other events, where event i happened before
 * i + n, where n >= 1. 
 * @author lee
 *
 */
public class UpdateChain implements IEvent{
	
	ArrayList<Update> updates = new ArrayList<Update>();
	
	public UpdateChain(){}
	
	public UpdateChain(Update update){
		addUpdate(update);
	}
	
	public void addUpdate(Update update){
		updates.add(update);
	}
	
	public List<Update> getUpdates(){
		return updates;
	}
	
	public void preFixChain(UpdateChain chain){
		ArrayList<Update> newUpdates = new ArrayList<Update>();
		
		newUpdates.addAll(chain.getUpdates());
		newUpdates.addAll(updates);
		updates = newUpdates;
	
	}

}
