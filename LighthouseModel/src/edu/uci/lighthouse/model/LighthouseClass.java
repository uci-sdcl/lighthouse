package edu.uci.lighthouse.model;

import javax.persistence.Entity;

@Entity
public class LighthouseClass extends LighthouseEntity {
	
	protected LighthouseClass() {
		this("");
	}

	public LighthouseClass(String fqn) {
		super(fqn);
	}
	
	public boolean isAnonymous(){
		return getFullyQualifiedName().matches("(\\w+\\.)*(\\w+\\$)+\\d+");
	}
	
	public boolean isInnerClass(){
		return getFullyQualifiedName().matches("(\\w+\\.)*(\\w+\\$)+[a-zA-Z_]+");
	}

	
	
}
