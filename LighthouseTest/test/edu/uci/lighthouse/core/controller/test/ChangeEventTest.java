package edu.uci.lighthouse.core.controller.test;

import java.util.Collections;
import java.util.LinkedHashSet;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;

import edu.uci.lighthouse.core.parser.IParserAction;
import edu.uci.lighthouse.core.parser.LighthouseParser;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseFile;
import edu.uci.lighthouse.model.LighthouseFileManager;
import edu.uci.lighthouse.parser.ParserException;

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

}
