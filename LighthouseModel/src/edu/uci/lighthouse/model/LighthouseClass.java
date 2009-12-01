package edu.uci.lighthouse.model;

import javax.persistence.Entity;

/**
 * Represents a Class for the Lighthouse Model.
 * 
 * @author tproenca
 * 
 */
@Entity
public class LighthouseClass extends LighthouseEntity {

	protected LighthouseClass() {
		this("");
	}

	public LighthouseClass(String fqn) {
		super(fqn);
	}

	public String getPackageName(){
		String result = getFullyQualifiedName().replaceAll("\\.\\w+\\z", "");
		return result.equals(getShortName()) ? "" : result;
	}
	
}
