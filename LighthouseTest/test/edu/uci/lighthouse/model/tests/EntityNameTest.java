package edu.uci.lighthouse.model.tests;

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
}
