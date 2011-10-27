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

import java.util.logging.Logger;

/**
 * Basic implementation to net.sf.statcvs logger.
 * 
 * @author Benoit Xhenseval
 * @version $Revision: 187 $
 */
public class JavaUtilTaskLogger implements TaskLogger {
	private static final Logger LOGGER = Logger.getLogger("net.sf.statcvs");

	/**
	 * log text to the logger.fine().
	 * 
	 * @param text
	 *            the text to log.
	 */
	public final void log(final String text) {
		LOGGER.fine(text);
	}

	/**
	 * log text to the logger.severe().
	 * 
	 * @param text
	 *            the text to log.
	 */
	public void error(final String arg) {
		LOGGER.severe(arg);
	}

	/**
	 * log text to the logger.info().
	 * 
	 * @param text
	 *            the text to log.
	 */
	public void info(final String arg) {
		LOGGER.info(arg);
	}
}
