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
package edu.uci.lighthouse.parser;

import java.util.Collection;
import java.util.LinkedList;


public class ParserEntity {

	public static enum EntityType {
		CLASS, INTERFACE, FIELD, METHOD, EXTERNAL_CLASS
	}
	
	private String fqn;
	private EntityType type;
	private Collection<String> listModifiers = new LinkedList<String>();

	public ParserEntity(String fqn, Collection<String> listModifiers, EntityType type) {
		this.fqn = fqn;
		this.listModifiers = listModifiers;
		this.type = type;
	}

	public String getFqn() {
		return fqn;
	}

	public EntityType getType() {
		return type;
	}

	public Collection<String> getListModifiers() {
		return listModifiers;
	}
	
}
