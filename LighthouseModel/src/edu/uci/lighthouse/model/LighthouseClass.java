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

//	public String getPackageName(){
//		String result = getFullyQualifiedName().replaceAll("\\.\\w+\\z", "");
//		return result.equals(getShortName()) ? "" : result;
//	}
	
}
