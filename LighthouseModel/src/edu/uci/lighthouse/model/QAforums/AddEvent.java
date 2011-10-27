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
