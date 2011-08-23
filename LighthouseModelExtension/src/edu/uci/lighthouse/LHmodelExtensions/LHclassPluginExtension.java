package edu.uci.lighthouse.LHmodelExtensions;

import java.io.Serializable;

import javax.persistence.Entity;

/**
 * 
 * @author lee
 *
 */
@Entity
public abstract class LHclassPluginExtension implements Serializable{
	
	// This is the ID from your extension point
	public static final String LHclassPluginExtension_ID = "edu.uci.lighthouse.LHmodelExtensions.LHclassPluginExtension";

}
