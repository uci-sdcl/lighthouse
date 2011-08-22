package edu.uci.lighthouse.LHmodelExtensions;

import java.io.Serializable;

/**
 * The Lighthouse Class Model can be extended by a plugin that extends
 * implementing this interface
 * @author lee
 *
 */
public abstract class LHclassPluginExtension implements Serializable{
	
	// This is the ID from your extension point
	public static final String LHclassPluginExtension_ID = "edu.uci.lighthouse.LHmodelExtensions.LHclassPluginExtension";

}
