package edu.uci.lighthouse.model.test;

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
}
