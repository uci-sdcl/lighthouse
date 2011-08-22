package edu.uci.lighthouse.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;


import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import edu.uci.lighthouse.LHmodelExtensions.ClassPluginLoader;
import edu.uci.lighthouse.LHmodelExtensions.ILHclassPluginExtension;



@Entity
public class LighthouseClass extends LighthouseEntity {
	
	private static final long serialVersionUID = 2097778395729254060L;
	
	//@author: Lee
	ArrayList<ILHclassPluginExtension> extensions = new ArrayList<ILHclassPluginExtension>(); 
	
	protected LighthouseClass() {
		this("");
		loadExtensions();
	}

	public LighthouseClass(String fqn) {
		super(fqn);
		loadExtensions();
	}
	
	public boolean isAnonymous(){
		return getFullyQualifiedName().matches("(\\w+\\.)*(\\w+\\$)+\\d+");
	}
	
	public boolean isInnerClass(){
		return getFullyQualifiedName().matches("(\\w+\\.)*(\\w+\\$)+[a-zA-Z_]+");
	}
	
	
	private void loadExtensions(){
		List<ILHclassPluginExtension> listOfExt = 
			ClassPluginLoader.getInstance().loadClassPluginExtensions();
		extensions.clear();
		extensions.addAll(listOfExt);
	}
	
}
