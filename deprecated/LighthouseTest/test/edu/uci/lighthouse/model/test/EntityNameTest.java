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
package edu.uci.lighthouse.model.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseMethod;

public class EntityNameTest extends TestCase{
	
	public void testShortName(){
		LighthouseEntity e;
		
		e = new LighthouseClass("java.lang.String");
		assertEquals("String", e.getShortName());
		
		e = new LighthouseClass("String");
		assertEquals("String", e.getShortName());
		
		e = new LighthouseMethod("Screen.displayMessages(java.lang.String)");
		assertEquals("displayMessages(String)", e.getShortName());
		
		e = new LighthouseMethod("Screen.<init>()");
		assertEquals("<init>()", e.getShortName());
		
		e = new LighthouseMethod("Screen.displayMessages(java.lang.String, java.util.List)");
		assertEquals("displayMessages(String, List)", e.getShortName());
	}
	
	public void testClassNature(){
		LighthouseClass c;
		
		c = new LighthouseClass("deitel.atm.ATM");
		assertFalse(c.isAnonymous());
		assertFalse(c.isInnerClass());
		
		c = new LighthouseClass("deitel.atm.ATM$Withdrawal");
		assertFalse(c.isAnonymous());
		assertTrue(c.isInnerClass());

		c = new LighthouseClass("deitel.atm.ATM$1");
		assertTrue(c.isAnonymous());
		assertFalse(c.isInnerClass());
		
		c = new LighthouseClass("deitel.atm.ATM$Withdrawal$1");
		assertTrue(c.isAnonymous());
		assertFalse(c.isInnerClass());
	}
	
	public void testPackageName(){
		LighthouseClass c;
		
		c = new LighthouseClass("ATMProject.ATM");
		assertEquals("", c.getPackageName());
		
		c = new LighthouseClass("ATMProject.deitel.atm.ATM");
		assertEquals("deitel.atm", c.getPackageName());
		
		c = new LighthouseClass("ATMProject.ATM$Withdrawal");
		assertEquals("", c.getPackageName());
		
		c = new LighthouseClass("ATMProject.deitel.atm.ATM$Withdrawal");
		assertEquals("deitel.atm", c.getPackageName());
		
		c = new LighthouseClass("ATMProject.ATM$1");
		assertEquals("", c.getPackageName());

		c = new LighthouseClass("ATMProject.deitel.atm.ATM$1");
		assertEquals("deitel.atm", c.getPackageName());
		
		c = new LighthouseClass("ATMProject.ATM$Withdrawal$1");
		assertEquals("", c.getPackageName());
		
		c = new LighthouseClass("ATMProject.deitel.atm.ATM$Withdrawal$1");
		assertEquals("deitel.atm", c.getPackageName());
	}
}
