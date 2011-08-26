package edu.uci.lighthouse.model.QAforums;

public class Update<T> implements IEvent{

	private T argument;
	
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
