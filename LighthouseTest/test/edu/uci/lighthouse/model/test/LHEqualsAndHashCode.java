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

import java.util.LinkedHashSet;
import java.util.Set;

import junit.framework.TestCase;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseMethod;
import edu.uci.lighthouse.model.LighthouseRelationship;

public class LHEqualsAndHashCode extends TestCase {

	public void testEqualsAndHashcode() {
		LighthouseMethod from1 = new LighthouseMethod("method from");
		LighthouseMethod to1 = new LighthouseMethod("method to");
		
		LighthouseMethod from2 = new LighthouseMethod("method from");
		LighthouseMethod to2 = new LighthouseMethod("method to");
		
		LighthouseRelationship rel1 = new LighthouseRelationship(from1,to1,LighthouseRelationship.TYPE.ACCESS); 
		LighthouseRelationship rel2 = new LighthouseRelationship(from2,to2,LighthouseRelationship.TYPE.ACCESS); 
		
		LighthouseEvent event1 = new LighthouseEvent(LighthouseEvent.TYPE.ADD,new LighthouseAuthor("Max"),rel1);
		LighthouseEvent event2 = new LighthouseEvent(LighthouseEvent.TYPE.ADD,new LighthouseAuthor("Max"),rel2);
		
		Set<LighthouseEvent> listA = new LinkedHashSet<LighthouseEvent>();
		Set<LighthouseEvent> listB = new LinkedHashSet<LighthouseEvent>();
		
		listA.add(event1);
		listB.add(event2);
		
		assertEquals(true,listA.containsAll(listB));
	}
}
