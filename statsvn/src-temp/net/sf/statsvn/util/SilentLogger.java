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
package net.sf.statsvn.util;

/**
 * Basic implementation to nothingness.
 *
 * @author Benoit Xhenseval
 * @version $Revision$
 */
public class SilentLogger implements TaskLogger {
	/**
	 * log text to the System.out.
	 * @param text the text to log.
	 */
	public final void log(final String text) { // NOPMD
	}

	public void error(final String arg) { // NOPMD
	}

	public void info(final String arg) { // NOPMD
	}
}
