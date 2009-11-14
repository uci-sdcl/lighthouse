package edu.uci.lighthouse.core.parser;

import edu.uci.ics.sourcerer.extractor.io.IRelationWriter;
import edu.uci.ics.sourcerer.util.io.PropertyManager;
import edu.uci.lighthouse.model.LighthouseRelationship.TYPE;

public class LighthouseRelationshipWriter implements IRelationWriter {

	private BuilderRelationship builderRelationship;

	public LighthouseRelationshipWriter(PropertyManager properties) {
		builderRelationship = BuilderRelationship.getInstance();
	}

	public void writeInside(String innerFqn, String outerFqn) {
		builderRelationship.addRelantionship(innerFqn, outerFqn, TYPE.INSIDE);
	}

	public void writeExtends(String subTypeFqn, String superTypeFqn) {
		builderRelationship.addRelantionship(subTypeFqn, superTypeFqn, TYPE.EXTENDS);
	}

	public void writeImplements(String subTypeFqn, String superTypeFqn) {
		builderRelationship.addRelantionship(subTypeFqn, superTypeFqn, TYPE.IMPLEMENTS);
	}

	public void writeReturns(String fqn, String returnType) {
		builderRelationship.addRelantionship(fqn, returnType, TYPE.RETURN);
	}

	public void writeHolds(String fqn, String type) {
		builderRelationship.addRelantionship(fqn, type, TYPE.HOLDS);
	}

	public void writeUses(String fqn, String type) {
		builderRelationship.addRelantionship(fqn, type, TYPE.USES);
	}

	public void writeCalls(String caller, String callee) {
		builderRelationship.addRelantionship(caller, callee, TYPE.CALL);
	}

	public void writeAccesses(String accessor, String field) {
		builderRelationship.addRelantionship(accessor, field, TYPE.ACCESS);
	}

	public void writeReceives(String fqn, String paramType, String paramName,
			int position) {
		builderRelationship.addRelantionship(fqn, paramType, TYPE.RECEIVES);
	}

	public void writeThrows(String fqn, String exceptionType) {
		builderRelationship.addRelantionship(fqn, exceptionType, TYPE.THROW);
	}

	public void writeAnnotates(String entity, String annotation) {

	}

	public void writeParametrizedBy(String fqn, String typeVariable, int pos) {

	}

	@Override
	public void close() {
	}

}
