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
package edu.uci.lighthouse.core.controller.test;

import junit.framework.TestCase;
import edu.uci.lighthouse.model.LighthouseFile;

public class ChangeEventTest extends TestCase {

	LighthouseFile oldLhFile = new LighthouseFile();
	LighthouseFile newLhFile = new LighthouseFile();
	
	public ChangeEventTest() {
		// TODO Auto-generated constructor stub
	}

	public ChangeEventTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	/*
	public void testChangeEvent() throws ParserException {
		final LighthouseParser parser = new LighthouseParser();
				
		final IFile oldIFile = null;
		
		parser.executeInAJob(Collections.singleton(oldIFile), new IParserAction() {
			@Override
			public void doAction() throws ParserException {
				oldLhFile = new LighthouseFileManager(oldLhFile).populateLHFile(
						parser.getListEntities(),
						parser.getListRelationships());
			}
		});
		
		final IFile newIFile = null;
		
		parser.executeInAJob(Collections.singleton(newIFile), new IParserAction() {
			@Override
			public void doAction() throws ParserException {
				newLhFile = new LighthouseFileManager(newLhFile).populateLHFile(
						parser.getListEntities(),
						parser.getListRelationships());
			}
		});
		
		LighthouseDelta delta = new LighthouseDelta(
				new LighthouseAuthor("Nilmax"),
				oldLhFile, newLhFile);
		
		LinkedHashSet<LighthouseEvent> events = delta.getEvents();
		
		for (LighthouseEvent event : events) {
			System.out.println(event);
		}
		
		assertTrue(events.size()!=0);
		
	}
*/
}
