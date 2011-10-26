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
package edu.uci.lighthouse.core.controller;

import java.util.Date;

public class TimestampLastEventReceived {

	private static TimestampLastEventReceived instance = null;
	private Date value = null;

	private TimestampLastEventReceived() {
		// Exists only to defeat instantiation.
	}

	public static TimestampLastEventReceived getInstance() {
		if (instance == null) {
			instance = new TimestampLastEventReceived();
		}
		return instance;
	}

	public void setValue(Date value) {
		this.value = value;
	}

	public Date getValue() {
		return value;
	}

}
