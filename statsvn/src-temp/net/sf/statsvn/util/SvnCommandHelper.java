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
/**
 * 
 */
package net.sf.statsvn.util;

import net.sf.statsvn.output.SvnConfigurationOptions;

/**
 * @author jpdaigle
 *
 * Utility class to help build svn command strings
 */
public final class SvnCommandHelper {
	private SvnCommandHelper() {
	}

	/**
	 * Gets the authentication / non-interactive command part to use when invoking
	 * the subversion binary.
	 * 
	 * @return A String with the username, password and non-interactive settings
	 */
	public static String getAuthString() {
		final StringBuffer strAuth = new StringBuffer(" --non-interactive");
		if (SvnConfigurationOptions.getSvnUsername() != null) {
			strAuth.append(" --username ").append(SvnConfigurationOptions.getSvnUsername()).append(" --password ").append(
			        SvnConfigurationOptions.getSvnPassword());
		}

		return strAuth.toString();
	}

}
