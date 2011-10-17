package edu.uci.lighthouse.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;


import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;

import edu.uci.lighthouse.LHmodelExtensions.ClassPluginLoader;
import edu.uci.lighthouse.LHmodelExtensions.LHclassPluginExtension;
import edu.uci.lighthouse.model.QAforums.LHforum;



@Entity
public class LighthouseClass extends LighthouseEntity {
	
	private static final long serialVersionUID = 2097778395729254060L;
	
	
	/**@author: Lee*/
	//ArrayList<LHclassPluginExtension> extensions = new ArrayList<LHclassPluginExtension>(); 
	
	/**@author lee*/
	 @OneToOne(cascade = CascadeType.ALL)
	private LHforum forum;
	
	protected LighthouseClass() {
		this("");
		forum = new LHforum();
	//	loadExtensions();
	}

	public LighthouseClass(String fqn) {
		super(fqn);
		forum = new LHforum();
	//	loadExtensions();
	}
	
	public boolean isAnonymous(){
		return getFullyQualifiedName().matches("(\\w+\\.)*(\\w+\\$)+\\d+");
	}
	
	public boolean isInnerClass(){
		return getFullyQualifiedName().matches("(\\w+\\.)*(\\w+\\$)+[a-zA-Z_]+");
	}
	

	/**
	 * @author lee
	 */
	/**private void loadExtensions(){
		List<LHclassPluginExtension> listOfExt = 
			ClassPluginLoader.getInstance().loadClassPluginExtensions();
		extensions.clear();
		extensions.addAll(listOfExt);
		
	}
	
	public List<LHclassPluginExtension> getExtensions(){
		return extensions;
	}**/

	public void setForum(LHforum forum) {
		this.forum = forum;
	}

	public LHforum getForum() {
		return forum;
	}
	
}
