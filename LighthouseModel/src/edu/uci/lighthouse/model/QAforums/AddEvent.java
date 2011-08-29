package edu.uci.lighthouse.model.QAforums;

import java.util.ArrayList;
import java.util.List;

public class AddEvent<T, G> implements IEvent{

	private T addition;
	private G reciever;
	
	public AddEvent(){}
	
	public AddEvent(T addition, G reciever){
		this.setAddition(addition);
		this.reciever = reciever;
	}


	public void setReciever(G reciever) {
		this.reciever = reciever;
	}

	public G getReciever() {
		return reciever;
	}

	public void setAddition(T addition) {
		this.addition = addition;
	}

	public T getAddition() {
		return addition;
	}


}
