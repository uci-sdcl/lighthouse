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

import junit.framework.TestCase;

/**
 * @author Benoit Xhenseval
 *
 */
public class SvnInfoUtilTest extends TestCase {
	public void testReplace() {
		assertEquals("String with no space", "thisisatest", StringUtils.replace(" ", "%20", "thisisatest"));
		assertEquals("null String", null, StringUtils.replace(" ", "%20", null));
		assertEquals("empty String", "", StringUtils.replace(" ", "%20", ""));
		assertEquals("null pattern", "thisisatest", StringUtils.replace(null, "%20", "thisisatest"));
		assertEquals("empty pattern", "thisisatest", StringUtils.replace("", "%20", "thisisatest"));
		assertEquals("1 pattern", "this%20isatest", StringUtils.replace(" ", "%20", "this isatest"));
		assertEquals("2 patterns", "this%20is%20atest", StringUtils.replace(" ", "%20", "this is atest"));
		assertEquals("3 patterns", "this%20is%20a%20test", StringUtils.replace(" ", "%20", "this is a test"));
		assertEquals("4 patterns", "%20this%20is%20a%20test", StringUtils.replace(" ", "%20", " this is a test"));
		assertEquals("5 patterns", "%20this%20is%20a%20%20test", StringUtils.replace(" ", "%20", " this is a  test"));
		assertEquals("6 patterns", "%20this%20is%20a%20%20test%20", StringUtils.replace(" ", "%20", " this is a  test "));
	}
}
