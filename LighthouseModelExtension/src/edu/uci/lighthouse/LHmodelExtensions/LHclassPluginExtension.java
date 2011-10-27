/*******************************************************************************
* Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
*				, University of California, Irvine}.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    {Software Design and Collaboration Laboratory (SDCL)
*	, University of California, Irvine}
*			- initial API and implementation and/or initial documentation
*******************************************************************************/
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
