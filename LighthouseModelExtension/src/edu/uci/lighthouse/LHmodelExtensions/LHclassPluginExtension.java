package edu.uci.lighthouse.LHmodelExtensions;

import java.io.Serializable;
import java.util.Observable;

import javax.persistence.Entity;

/**
 * 
 * @author lee
 *
 */
@Entity
public abstract class LHclassPluginExtension extends Observable implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2607760725675842048L;
	// This is the ID from your extension point
	public static final String LHclassPluginExtension_ID = "edu.uci.lighthouse.LHmodelExtensions.LHclassPluginExtension";

}
