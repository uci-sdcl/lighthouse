package edu.uci.lighthouse.model.QAforums;

import java.lang.reflect.Field;

public class UpdateEvent<T> implements IEvent{
	private T updatedObject;
	private Field fieldUpdated;
	
	public UpdateEvent(T updatedObject, Field fieldUpdated){
		this.setUpdatedObject(updatedObject);
		this.setFieldUpdated(fieldUpdated);
		

	}

	public void setFieldUpdated(Field fieldUpdated) {
		this.fieldUpdated = fieldUpdated;
	}

	public Field getFieldUpdated() {
		return fieldUpdated;
	}

	public void setUpdatedObject(T updatedObject) {
		this.updatedObject = updatedObject;
	}

	public T getUpdatedObject() {
		return updatedObject;
	}
	
}
