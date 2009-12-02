package edu.uci.lighthouse.parser;


public class ParserEntity {

	public static enum EntityType {
		CLASS, INTERFACE, FIELD, METHOD, EXTERNAL_CLASS
	}
	
	private String fqn;
	private int modifiers;
	private EntityType type;
	
	public ParserEntity(String fqn, int modifiers, EntityType type) {
		this.fqn = fqn;
		this.modifiers = modifiers;
		this.type = type;
	}

	public String getFqn() {
		return fqn;
	}

	public int getModifiers() {
		return modifiers;
	}

	public EntityType getType() {
		return type;
	}
	
}
