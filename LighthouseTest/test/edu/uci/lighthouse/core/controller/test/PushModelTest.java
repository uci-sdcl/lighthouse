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

public class PushModelTest extends TestCase {

//	public void testUpdateModelFromDelta() throws DocumentException, IOException, JPAException {
//		LighthouseModelTest xmlModel = new LighthouseModelTest();
//		new LighthouseModelXMLPersistence(xmlModel).load(LHTestDataFiles.XML_LH_MODEL);
//		
//		// Save LH Model Into the database in order to help the
//		// BuilderRelationship to handle the External classes
////		new LighthouseModelManager(xmlModel).saveEventsIntoDatabase(xmlModel.getListEvents());
//		
//		// Load delta from xml file
//		LighthouseDelta xmlDelta = new LighthouseDelta();
//		//new LHDeltaXMLPersistence(xmlDelta).load(LHTestDataFiles.XML_DELTA_ATM_CLASS);
//		
//		// Update Model
//		//new PushModel(xmlModel).updateModelFromDelta(xmlDelta);
//		
//		List<LighthouseEvent> listEvents = new LHEventDAO().list();
//		LighthouseModelTest databaseModel = new LighthouseModelTest();
//		for (LighthouseEvent event : listEvents) {
//			new LighthouseModelManager(databaseModel).addEvent(event);
//		}
//		
//		// Load LH Updated Model in XML file
//		LighthouseModelTest xmlUpdatedModel = new LighthouseModelTest();
//		new LighthouseModelXMLPersistence(xmlUpdatedModel).load(LHTestDataFiles.XML_UPDATED_MODEL);
//		
//		assertEquals(true, databaseModel.getListEvents().containsAll(xmlUpdatedModel.getListEvents()));
//	}
//	
//	public void testUpdateCommittedEvents() throws DocumentException, JPAException {
////		LighthouseModelTest xmlModel = new LighthouseModelTest();
////		new LighthouseModelXMLPersistence(xmlModel).load(LHTestDataFiles.XML_LH_MODEL);
////		
////		// Save LH Model Into the database in order to help the
////		// BuilderRelationship to handle the External classes
////		new LighthouseModelManager(xmlModel).saveEventsIntoDatabase(xmlModel.getListEvents());
////		
////		List<String> listClazzFqn = new ArrayList<String>();
////		listClazzFqn.add("edu.prenticehall.deitel.Screen");
////		listClazzFqn.add("edu.prenticehall.deitel.Transaction");
////		new PushModel(xmlModel).updateCommittedEvents(listClazzFqn, "Max");
////		
////		String command = "SELECT e FROM LighthouseEvent e WHERE e.isCommitted=1";
////		List<LighthouseEvent> listEvents = new LHEventDAO().executeDynamicQuery(command);
////		assertEquals(true, listEvents.size()==12);
//	}

}
