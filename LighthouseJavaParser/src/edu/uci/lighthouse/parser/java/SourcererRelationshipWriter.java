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

import edu.uci.ics.sourcerer.extractor.io.IRelationWriter;
import edu.uci.ics.sourcerer.util.io.PropertyManager;
import edu.uci.lighthouse.parser.ParserRelationship;

public class SourcererRelationshipWriter implements IRelationWriter {

	private SourcererOutput sourcererOutput;

	public SourcererRelationshipWriter(PropertyManager properties) {
		sourcererOutput = SourcererOutput.getInstance();
	}

	public void writeInside(String innerFqn, String outerFqn) {
		sourcererOutput.addRelantionship(innerFqn, outerFqn, ParserRelationship.RelationshipType.INSIDE);
	}

	public void writeExtends(String subTypeFqn, String superTypeFqn) {
		sourcererOutput.addRelantionship(subTypeFqn, superTypeFqn, ParserRelationship.RelationshipType.EXTENDS);
	}

	public void writeImplements(String subTypeFqn, String superTypeFqn) {
		sourcererOutput.addRelantionship(subTypeFqn, superTypeFqn, ParserRelationship.RelationshipType.IMPLEMENTS);
	}

	public void writeReturns(String fqn, String returnType) {
		sourcererOutput.addRelantionship(fqn, returnType, ParserRelationship.RelationshipType.RETURN);
	}

	public void writeHolds(String fqn, String type) {
		sourcererOutput.addRelantionship(fqn, type, ParserRelationship.RelationshipType.HOLDS);
	}

	public void writeUses(String fqn, String type) {
		sourcererOutput.addRelantionship(fqn, type, ParserRelationship.RelationshipType.USES);
	}

	public void writeCalls(String caller, String callee) {
		sourcererOutput.addRelantionship(caller, callee, ParserRelationship.RelationshipType.CALL);
	}

	public void writeAccesses(String accessor, String field) {
		sourcererOutput.addRelantionship(accessor, field, ParserRelationship.RelationshipType.ACCESS);
	}

	public void writeReceives(String fqn, String paramType, String paramName,
			int position) {
		sourcererOutput.addRelantionship(fqn, paramType, ParserRelationship.RelationshipType.RECEIVES);
	}

	public void writeThrows(String fqn, String exceptionType) {
		sourcererOutput.addRelantionship(fqn, exceptionType, ParserRelationship.RelationshipType.THROW);
	}

	public void writeAnnotates(String entity, String annotation) {

	}

	public void writeParametrizedBy(String fqn, String typeVariable, int pos) {

	}

	@Override
	public void close() {
	}

}
