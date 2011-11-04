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

import edu.uci.ics.sourcerer.extractor.io.IEntityWriter;
import edu.uci.ics.sourcerer.util.io.PropertyManager;
import edu.uci.lighthouse.parser.ParserEntity;

public final class SourcererEntityWriter implements IEntityWriter {

	private SourcererOutput sourcererOutput;

	public SourcererEntityWriter(PropertyManager properties) {
		sourcererOutput = SourcererOutput.getInstance();
	}

	public void writeClass(String fqn, int modifiers, String filename, int startPos, int length) {			
		sourcererOutput.addEntity(fqn, modifiers, ParserEntity.EntityType.CLASS);
	} 

	public void writeInterface(String fqn, int modifiers, String filename, int startPos, int length) {
		sourcererOutput.addEntity(fqn, modifiers, ParserEntity.EntityType.INTERFACE);
	}

	public void writeConstructor(String fqn, int modifiers, String filename, int startPos, int length) {
		sourcererOutput.addEntity(fqn, modifiers, ParserEntity.EntityType.METHOD);
	}

	public void writeMethod(String fqn, int modifiers, String filename, int startPos, int length) {
		sourcererOutput.addEntity(fqn, modifiers, ParserEntity.EntityType.METHOD);
	}

	public void writeField(String fqn, int modifiers, String filename, int startPos, int length) {
		sourcererOutput.addEntity(fqn, modifiers, ParserEntity.EntityType.FIELD);
	}

	public void writeEnum(String fqn, int modifiers, String filename, int startPos, int length) {
		sourcererOutput.addEntity(fqn, modifiers, ParserEntity.EntityType.EXTERNAL_CLASS);
	}

	public void writeInitializer(String fqn, int modifiers, String filename,
			int startPos, int length) {
	}

	public void writeEnumConstant(String fqn, int modifiers, String filename,
			int startPos, int length) {
	}

	public void writeAnnotation(String fqn, int modifiers, String filename,
			int startPos, int length) {
	}

	public void writeAnnotationMember(String fqn, int modifiers,
			String filename, int startPos, int length) {
	}

	@Override
	public void close() {
	}
	
}
