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
 * An Interface for the Logging mechanism.
 * @author Benoit Xhenseval
 */
public interface TaskLogger {
	/**
	 * Generic interface for logging debug info.
	 * @param arg the string to log.
	 */
	void log(String arg);

	/**
	 * Generic interface for logging info.
	 * @param arg the string to log.
	 */
	void info(String arg);

	/**
	 * Generic interface for logging error.
	 * @param arg the string to log.
	 */
	void error(String arg);
}
