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
package edu.uci.lighthouse.core.dbactions;

import java.util.LinkedList;

import edu.uci.lighthouse.core.util.WorkbenchUtility;
import edu.uci.lighthouse.model.io.IPersistable;

public class DatabaseActionsBuffer extends LinkedList<IDatabaseAction> implements IPersistable {

	private static final long serialVersionUID = -5094074685177488970L;
	
	private static final String filename = "lighthouse_buffer.bin";
	
	@Override
	public synchronized IDatabaseAction peek() {
		return super.peek();
	}

	@Override
	public synchronized IDatabaseAction poll() {
		return super.poll();
	}

	@Override
	public synchronized boolean offer(IDatabaseAction e) {
		return super.offer(e);
	}

	@Override
	public String getFileName() {
		return WorkbenchUtility.getMetadataDirectory() + filename;
	}	
}
