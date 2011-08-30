package edu.uci.lighthouse.model.QAforums;

public class UpdateEvent<T,G> implements IEvent{
	private T updatedObject;
	private G fieldUpdated;
	
	public UpdateEvent(T updatedObject, G fieldUpdated){
		this.setUpdatedObject(updatedObject);
		this.setFieldUpdated(fieldUpdated);
	}

	public void setFieldUpdated(G fieldUpdated) {
		this.fieldUpdated = fieldUpdated;
	}

	public G getFieldUpdated() {
		return fieldUpdated;
	}

	public void setUpdatedObject(T updatedObject) {
		this.updatedObject = updatedObject;
	}

	public T getUpdatedObject() {
		return updatedObject;
	}
	
}
