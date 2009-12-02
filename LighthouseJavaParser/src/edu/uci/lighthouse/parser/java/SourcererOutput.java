package edu.uci.lighthouse.parser.java;

import java.util.ArrayList;
import java.util.Collection;

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
		ParserEntity entity = new ParserEntity(fqn, modifiers, entityType);
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

}
