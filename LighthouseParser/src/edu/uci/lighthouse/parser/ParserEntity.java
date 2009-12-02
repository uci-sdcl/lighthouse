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
