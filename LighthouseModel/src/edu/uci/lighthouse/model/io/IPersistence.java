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
package edu.uci.lighthouse.model.io;

import javax.persistence.PersistenceException;

public interface IPersistence {

	public static int XML = 0;
	
	public static int BINARY = 1;
	
	public void save() throws PersistenceException;
	
	public void save(String fileName) throws PersistenceException;
	
	public void load() throws PersistenceException;
	
	public void load(String fileName) throws PersistenceException;
	
}
