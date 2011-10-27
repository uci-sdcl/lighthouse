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
package edu.uci.lighthouse.parser.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import edu.uci.ics.sourcerer.model.Modifier;
import edu.uci.lighthouse.parser.ParserEntity;
import edu.uci.lighthouse.parser.ParserRelationship;
import edu.uci.lighthouse.parser.ParserEntity.EntityType;
import edu.uci.lighthouse.parser.ParserRelationship.RelationshipType;

public class SourcererOutput {

	private static SourcererOutput instance = null;

	private Collection<ParserEntity> entities = new ArrayList<ParserEntity>();
	private Collection<ParserRelationship> relationships = new ArrayList<ParserRelationship>();

	private SourcererOutput() {

	}

	public static SourcererOutput getInstance() {
		if (instance == null) {
			instance = new SourcererOutput();
		}
		return instance;
	}

	public void addEntity(String fqn, int modifiers, EntityType entityType) {
		ParserEntity entity = new ParserEntity(fqn, getModifiers(modifiers), entityType);
		entities.add(entity);
	}

	public void addRelantionship(String fqnFrom, String fqnTo,
			RelationshipType type) {
		ParserRelationship rel = new ParserRelationship(fqnFrom, fqnTo, type);
		relationships.add(rel);
	}

	public Collection<ParserEntity> getEntities() {
		return entities;
	}

	public Collection<ParserRelationship> getRelationships() {
		return relationships;
	}

	/**
	 * before populate this, we need to clean the list of entities and
	 * relationships because this is a singleton instance
	 */
	public void clear() {
		entities.clear();
		relationships.clear();
	}

	private Collection<String> getModifiers(int modifiers) {
		Collection<String> resultList = new LinkedList<String>();
		Set<Modifier> setModifiers = Modifier.convertFromInt(modifiers);
		for (Modifier utilModifier : setModifiers) {
			resultList.add(utilModifier.toString());
		}
		return resultList;
	}
		
}
