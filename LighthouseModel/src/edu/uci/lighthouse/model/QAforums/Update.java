package edu.uci.lighthouse.model.QAforums;

import java.util.ArrayList;
import java.util.List;

public class Update<T> implements IEvent{

	private T argument;

	
    ArrayList<Update> causalUpdates = new ArrayList<Update>();
	
	public void addCausalUpdate(Update update){
		causalUpdates.add(update);
	}
	
	public List<Update> getcausalUpdates(){
		return causalUpdates;
	}
	
	
	public Update(){}
	
	public Update(T argument){
		this.argument = argument;
	}

	public void setArgument(T argument) {
		this.argument = argument;
	}

	public T getArgument() {
		return argument;
	}


}
