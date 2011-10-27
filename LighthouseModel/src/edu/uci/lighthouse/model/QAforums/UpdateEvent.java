/*******************************************************************************
* Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
*				, University of California, Irvine}.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    {Software Design and Collaboration Laboratory (SDCL)
*	, University of California, Irvine}
*			- initial API and implementation and/or initial documentation
*******************************************************************************/
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
