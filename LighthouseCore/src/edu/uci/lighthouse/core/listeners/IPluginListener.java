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
package edu.uci.lighthouse.core.listeners;

import org.osgi.framework.BundleContext;

public interface IPluginListener {
	/**
	 * This method is called upon plug-in activation
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin;
	 */
	public void start(BundleContext context) throws Exception;

	/**
	 * This method is called when the plug-in is stopped
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin;
	 */
	public void stop(BundleContext context) throws Exception;
}
