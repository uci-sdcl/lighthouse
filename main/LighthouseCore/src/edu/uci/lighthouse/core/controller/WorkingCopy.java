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
import java.util.HashMap;

public class WorkingCopy extends HashMap<String, Date> /*implements IPersistable*/ {

	private static final long serialVersionUID = -1572792397908630831L;
	
	/*private static final String filename = "workingCopy.bin";
	
	public String getFileName() {
		return WorkbenchUtility.getMetadataDirectory() + filename;
	}*/
}
