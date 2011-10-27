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

import java.io.PrintStream;

/**
 * Basic implementation to System.out.
 * 
 * @author Benoit Xhenseval
 * @version $Revision: 187 $
 */
public class ConsoleTaskLogger implements TaskLogger {
	private static final PrintStream STREAM = System.out;

	/**
	 * log text to the System.out.
	 * 
	 * @param text
	 *            the text to log.
	 */
	public final void log(final String text) {
		STREAM.println(text);
	}

	public void error(final String arg) {
		log(arg);
	}

	public void info(final String arg) {
		log(arg);
	}
}
