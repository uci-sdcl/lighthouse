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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.dom4j.DocumentException;

import edu.uci.lighthouse.model.BuildLHBaseFile;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseFile;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.io.LighthouseFileXMLPersistence;
import edu.uci.lighthouse.model.io.LighthouseModelXMLPersistence;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.util.LHStringUtil;
import edu.uci.lighthouse.test.util.LHTestDataFiles;
import edu.uci.lighthouse.test.util.LighthouseModelTest;

public class LighthouseFileManagerTest extends TestCase {

	public void testGetBaseLHFile() throws DocumentException, ParseException, JPAException {
		
		LighthouseModelTest xmlModel = new LighthouseModelTest();
		new LighthouseModelXMLPersistence(xmlModel).load(LHTestDataFiles.XML_UPDATED_MODEL);
		
		Date revisionTime = LHStringUtil.simpleDateFormat.parse("2009-10-22 01:26:35");
		
		for (LighthouseEvent event : xmlModel.getListEvents()) {
			event.setCommitted(true);
			Object artifact = event.getArtifact();
			if (artifact instanceof LighthouseEntity) {
				LighthouseEntity entity = (LighthouseEntity) artifact;
				if (entity.getFullyQualifiedName().indexOf("$")!=-1) {
					// It is a "Inner" entity - I am doing this just to have some entities that are not committed
					event.setCommitted(false);
					event.setCommittedTime(revisionTime);
				}
			} else if (artifact instanceof LighthouseRelationship) {
				LighthouseRelationship rel = (LighthouseRelationship) artifact;
				LighthouseEntity entityFrom = rel.getFromEntity();
				if (entityFrom.getFullyQualifiedName().indexOf("$")!=-1) {
					// It is a "Inner" rel - I am doing this just to have some rel that are not committed
					event.setCommitted(false);
					event.setCommittedTime(revisionTime);
				}
			}
		}
		
		// Save LH Model Into the database in order to help the
		// BuilderRelationship to handle the External classes
//		new LighthouseModelManager(xmlModel).saveEventsIntoDatabase(xmlModel.getListEvents());
		
		LighthouseAuthor author = new LighthouseAuthor("Max");
		
		LighthouseFile lhBaseFile = BuildLHBaseFile.execute(xmlModel,"edu.prenticehall.deitel.ATM",revisionTime,author);
		
		LighthouseFile xmlLHFile = new LighthouseFile();
		new LighthouseFileXMLPersistence(xmlLHFile).load(LHTestDataFiles.XML_ATM_JAVA_MODIFIED);
		
		LighthouseDelta delta = new LighthouseDelta(author, lhBaseFile, xmlLHFile);
		
		assertEquals(0, delta.getEvents().size());
	}
	
}
