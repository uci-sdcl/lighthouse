package edu.uci.lighthouse.model;

import java.util.ArrayList;

import javax.persistence.Entity;

import edu.uci.lighthouse.model.extensions.ILHclassPluginExtension;


@Entity
public class LighthouseClass extends LighthouseEntity {
	
	private static final long serialVersionUID = 2097778395729254060L;
	
	//Lee
	ArrayList<ILHclassPluginExtension> extensionPoints = new ArrayList<ILHclassPluginExtension>(); 
	
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
