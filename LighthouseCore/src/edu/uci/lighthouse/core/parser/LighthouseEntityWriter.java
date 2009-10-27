package edu.uci.lighthouse.core.parser;

import edu.uci.ics.sourcerer.extractor.io.IEntityWriter;
import edu.uci.ics.sourcerer.util.io.PropertyManager;
import edu.uci.lighthouse.core.parser.BuilderEntity.EntityType;

public final class LighthouseEntityWriter implements IEntityWriter {

	private BuilderEntity builderEntity;

	public LighthouseEntityWriter(PropertyManager properties) {
		builderEntity = BuilderEntity.getInstance();
	}

	public void writeClass(String fqn, int modifiers, String filename, int startPos, int length) {			
		builderEntity.addEntity(fqn, modifiers, EntityType.CLASS);
	}

	public void writeInterface(String fqn, int modifiers, String filename, int startPos, int length) {
		builderEntity.addEntity(fqn, modifiers, EntityType.INTERFACE);
	}

	public void writeConstructor(String fqn, int modifiers, String filename, int startPos, int length) {
		builderEntity.addEntity(fqn, modifiers, EntityType.METHOD);
	}

	public void writeMethod(String fqn, int modifiers, String filename, int startPos, int length) {
		builderEntity.addEntity(fqn, modifiers, EntityType.METHOD);
	}

	public void writeField(String fqn, int modifiers, String filename, int startPos, int length) {
		builderEntity.addEntity(fqn, modifiers, EntityType.FIELD);
	}

	public void writeEnum(String fqn, int modifiers, String filename, int startPos, int length) {
		builderEntity.addEntity(fqn, modifiers, EntityType.EXTERNAL_CLASS);
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
