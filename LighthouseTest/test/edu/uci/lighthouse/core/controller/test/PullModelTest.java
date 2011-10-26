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

import java.text.ParseException;

import junit.framework.TestCase;

import org.dom4j.DocumentException;

import edu.uci.lighthouse.model.jpa.JPAException;

public class PullModelTest extends TestCase {

	public void testExecuteQueryTimeout() throws ParseException, JPAException {
//		LighthouseModelTest model = new LighthouseModelTest();
//		LighthouseModelManager lighthouseManager = new LighthouseModelManager(model);
//		LHEventDAO dao = new LHEventDAO();
//		
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		
//		LighthouseClass clazz_A = new LighthouseClass("Class A");
//		LighthouseField field_A = new LighthouseField("Field A");
//		LighthouseRelationship rel_A = new LighthouseRelationship(field_A,clazz_A,LighthouseRelationship.TYPE.INSIDE);
//		
//		LighthouseEvent eventClazz_A = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, clazz_A);
//		eventClazz_A.setTimestamp(formatter.parse("2009-12-01 01:00:00"));
//		lighthouseManager.addEvent(eventClazz_A);
//		dao.save(eventClazz_A);
//		
//		LighthouseEvent eventField_A = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, field_A);
//		eventField_A.setTimestamp(formatter.parse("2009-12-01 01:30:00"));
//		lighthouseManager.addEvent(eventField_A);
//		dao.save(eventField_A);
//		
//		LighthouseEvent eventRel_A = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, rel_A);
//		eventRel_A.setTimestamp(formatter.parse("2009-12-01 02:00:00"));
//		lighthouseManager.addEvent(eventRel_A);
//		dao.save(eventRel_A);
//		
//		PullModel pullModel = new PullModel(model);
//		List<LighthouseEvent> listEvent = pullModel.executeQueryTimeout(formatter.parse("2009-12-01 01:30:00"));
//		
//		assertEquals(true,listEvent.contains(eventField_A) && listEvent.contains(eventRel_A));
	}
	
	public void testExecuteQueryEventAfterCheckout() throws JPAException, ParseException, DocumentException {
//		LighthouseModelTest xmlModel = new LighthouseModelTest();
//		new LighthouseModelXMLPersistence(xmlModel).load(LHTestDataFiles.XML_LH_MODEL);
//		
//		// Save LH Model Into the database in order to help the
//		// BuilderRelationship to handle the External classes
////		new LighthouseModelManager(xmlModel).saveEventsIntoDatabase(xmlModel.getListEvents());
//
//		// TODO na esquece de subir o banco
//		
//		LighthouseModelTest model = new LighthouseModelTest();
//		PullModel pullModel = new PullModel(model);
//		HashMap<String, Date> map = new HashMap<String, Date>();
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date committedDate = formatter.parse("2009-11-03 20:30:00");
//		map.put("edu.prenticehall.deitel.Screen", committedDate);
//		
//		pullModel.executeQueryCheckout(map);
		
//		LighthouseModelTest model = new LighthouseModelTest();
//		LighthouseModelManager lighthouseManager = new LighthouseModelManager(model);
//		LHEventDAO dao = new LHEventDAO();
//		
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		
//		LighthouseClass mainClass = new LighthouseClass("Main Class");
//		LighthouseField fieldComittedBeforeRevisionTime = new LighthouseField("Committed Before Revision Time");
//		LighthouseField fieldCreatedBeforeRevisionWithoutCommit = new LighthouseField("Created Before Revision Without Commit");
//		LighthouseField fieldCreatedAfterRevision = new LighthouseField("Created After Revision");
//		LighthouseField fieldRemovedAndCommittedBeforeRevision = new LighthouseField("Removed and Committed Before Revision");
//		LighthouseField fieldNotInside = new LighthouseField("Field NOT Inside");
//		LighthouseField fieldRemoveAndCommittedAfterRevision = new LighthouseField("Removed And Committed After Revision");
//		LighthouseField fieldModifyWithoutAdd = new LighthouseField("Modify Without Add - Weird");
//		
//		// It WILL show up in the result
//		LighthouseEvent eventMainClass = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, mainClass);
//		eventMainClass.setTimestamp(formatter.parse("2009-12-01 00:40:00"));
//		eventMainClass.setCommittedTime(formatter.parse("2009-12-01 00:50:00"));
//		lighthouseManager.addEvent(eventMainClass);
//		dao.save(eventMainClass);
//		
//		// It will show up in the result - ADDED and COMITTED
//		LighthouseEvent eventfieldComittedBeforeRevisionTime = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, fieldComittedBeforeRevisionTime);
//		eventfieldComittedBeforeRevisionTime.setTimestamp(formatter.parse("2009-12-01 00:40:00"));
//		eventfieldComittedBeforeRevisionTime.setCommittedTime(formatter.parse("2009-12-01 00:50:00"));
//		lighthouseManager.addEvent(eventfieldComittedBeforeRevisionTime);
//		dao.save(eventfieldComittedBeforeRevisionTime);
//		
//		// It will NOT show up in the result - ADDED before revisionTime without commit
//		LighthouseEvent eventFieldCreatedBeforeRevisionWithoutCommit = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, fieldCreatedBeforeRevisionWithoutCommit);
//		eventFieldCreatedBeforeRevisionWithoutCommit.setTimestamp(formatter.parse("2009-12-01 00:40:00"));
//		lighthouseManager.addEvent(eventFieldCreatedBeforeRevisionWithoutCommit);
//		dao.save(eventFieldCreatedBeforeRevisionWithoutCommit);
//		
//		// It WILL show up in the result
//		LighthouseEvent eventfieldCreatedAfterRevision = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, fieldCreatedAfterRevision);
//		eventfieldCreatedAfterRevision.setTimestamp(formatter.parse("2009-12-01 01:10:00"));
//		lighthouseManager.addEvent(eventfieldCreatedAfterRevision);
//		dao.save(eventfieldCreatedAfterRevision);
//		
//		// It will NOT show up in the result - TYPE.REMOVE and COMITTED_TIME
//		LighthouseEvent eventfieldRemovedAndCommitted = new LighthouseEvent(LighthouseEvent.TYPE.REMOVE, LHPreference.author, fieldRemovedAndCommittedBeforeRevision);
//		eventfieldRemovedAndCommitted.setTimestamp(formatter.parse("2009-12-01 00:40:00"));
//		eventfieldRemovedAndCommitted.setCommittedTime(formatter.parse("2009-12-01 00:50:00"));
//		lighthouseManager.addEvent(eventfieldRemovedAndCommitted);
//		dao.save(eventfieldRemovedAndCommitted);
//		
//		// It will NOT show up in the result - NOT inside
//		LighthouseEvent eventFieldNotInside = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, fieldNotInside);
//		eventFieldNotInside.setTimestamp(formatter.parse("2009-12-01 01:20:00"));
//		lighthouseManager.addEvent(eventFieldNotInside);
//		dao.save(eventFieldNotInside);
//		
//		// It WILL show up in the result
//		LighthouseEvent eventFieldRemoveAndCommittedAfterRevision = new LighthouseEvent(LighthouseEvent.TYPE.REMOVE, LHPreference.author, fieldRemoveAndCommittedAfterRevision);
//		eventFieldRemoveAndCommittedAfterRevision.setTimestamp(formatter.parse("2009-12-01 00:30:00"));
//		eventFieldRemoveAndCommittedAfterRevision.setCommittedTime(formatter.parse("2009-12-01 01:35:00"));
//		lighthouseManager.addEvent(eventFieldRemoveAndCommittedAfterRevision);
//		dao.save(eventFieldRemoveAndCommittedAfterRevision);
//		
//		// It WILL show up in the result
//		LighthouseEvent eventFieldModifyWithoutAdd = new LighthouseEvent(LighthouseEvent.TYPE.MODIFY, LHPreference.author, fieldModifyWithoutAdd);
//		eventFieldModifyWithoutAdd.setTimestamp(formatter.parse("2009-12-01 01:31:00"));
//		lighthouseManager.addEvent(eventFieldModifyWithoutAdd);
//		dao.save(eventFieldModifyWithoutAdd);
//		
//		LighthouseRelationship relComittedBeforeRevisionTime = new LighthouseRelationship(fieldComittedBeforeRevisionTime,mainClass,LighthouseRelationship.TYPE.INSIDE);
//		LighthouseRelationship relCreatedBeforeRevisionWithoutCommit = new LighthouseRelationship(fieldCreatedBeforeRevisionWithoutCommit,mainClass,LighthouseRelationship.TYPE.INSIDE);
//		LighthouseRelationship relCreatedAfterRevision = new LighthouseRelationship(fieldCreatedAfterRevision,mainClass,LighthouseRelationship.TYPE.INSIDE);
//		LighthouseRelationship relRemoveAndCommittedBeforeRevision = new LighthouseRelationship(fieldRemovedAndCommittedBeforeRevision,mainClass,LighthouseRelationship.TYPE.INSIDE);
//		LighthouseRelationship relNotInside = new LighthouseRelationship(fieldNotInside,mainClass,LighthouseRelationship.TYPE.USES); // not inside
//		LighthouseRelationship relRemoveAndCommittedAfterRevision = new LighthouseRelationship(fieldRemoveAndCommittedAfterRevision,mainClass,LighthouseRelationship.TYPE.INSIDE);
//		LighthouseRelationship relModifyWithoutAdd = new LighthouseRelationship(fieldModifyWithoutAdd,mainClass,LighthouseRelationship.TYPE.INSIDE);
//		
//		// It will show up in the result - ADDED and COMITTED
//		LighthouseEvent eventRelComittedBeforeRevisionTime = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, relComittedBeforeRevisionTime);
//		eventRelComittedBeforeRevisionTime.setTimestamp(formatter.parse("2009-12-01 00:40:00"));
//		eventRelComittedBeforeRevisionTime.setCommittedTime(formatter.parse("2009-12-01 00:50:00"));
//		lighthouseManager.addEvent(eventRelComittedBeforeRevisionTime);
//		dao.save(eventRelComittedBeforeRevisionTime);
//		
//		// It will NOT show up in the result - ADDED before revisionTime without commit
//		LighthouseEvent eventRelCreatedBeforeRevisionWithoutCommit = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, relCreatedBeforeRevisionWithoutCommit);
//		eventRelCreatedBeforeRevisionWithoutCommit.setTimestamp(formatter.parse("2009-12-01 00:40:00"));
//		lighthouseManager.addEvent(eventRelCreatedBeforeRevisionWithoutCommit);
//		dao.save(eventRelCreatedBeforeRevisionWithoutCommit);
//		
//		// It WILL show up in the result
//		LighthouseEvent eventRelCreatedAfterRevision = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, relCreatedAfterRevision);
//		eventRelCreatedAfterRevision.setTimestamp(formatter.parse("2009-12-01 01:10:00"));
//		lighthouseManager.addEvent(eventRelCreatedAfterRevision);
//		dao.save(eventRelCreatedAfterRevision);
//		
//		// It will NOT show up in the result - TYPE.REMOVE and COMITTED_TIME
//		LighthouseEvent eventRelRemoveAndCommittedBeforeRevision = new LighthouseEvent(LighthouseEvent.TYPE.REMOVE, LHPreference.author, relRemoveAndCommittedBeforeRevision);
//		eventRelRemoveAndCommittedBeforeRevision.setTimestamp(formatter.parse("2009-12-01 00:40:00"));
//		eventRelRemoveAndCommittedBeforeRevision.setCommittedTime(formatter.parse("2009-12-01 00:50:00"));
//		lighthouseManager.addEvent(eventRelRemoveAndCommittedBeforeRevision);
//		dao.save(eventRelRemoveAndCommittedBeforeRevision);
//		
//		//It will NOT show because is not inside (so, I am not looking for this entity) 
//		LighthouseEvent eventRelNotInside = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, relNotInside);
//		eventRelNotInside.setTimestamp(formatter.parse("2009-12-01 01:20:00"));
//		lighthouseManager.addEvent(eventRelNotInside);
//		dao.save(eventRelNotInside);
//		
//		// It WILL show up in the result
//		LighthouseEvent eventRelRemoveAndCommittedAfterRevision = new LighthouseEvent(LighthouseEvent.TYPE.REMOVE, LHPreference.author, relRemoveAndCommittedAfterRevision);
//		eventRelRemoveAndCommittedAfterRevision.setTimestamp(formatter.parse("2009-12-01 00:30:00"));
//		eventRelRemoveAndCommittedAfterRevision.setCommittedTime(formatter.parse("2009-12-01 01:35:00"));
//		lighthouseManager.addEvent(eventRelRemoveAndCommittedAfterRevision);
//		dao.save(eventRelRemoveAndCommittedAfterRevision);
//		
//		// It WILL show up in the result
//		LighthouseEvent eventRelModifyWithoutAdd = new LighthouseEvent(LighthouseEvent.TYPE.MODIFY, LHPreference.author, relModifyWithoutAdd);
//		eventRelModifyWithoutAdd.setTimestamp(formatter.parse("2009-12-01 01:31:00"));
//		lighthouseManager.addEvent(eventRelModifyWithoutAdd);
//		dao.save(eventRelModifyWithoutAdd);
//		
//		HashMap<String, Date> mapClassFqnToLastRevisionTimestamp = new HashMap<String, Date>();
//		mapClassFqnToLastRevisionTimestamp.put(mainClass.getFullyQualifiedName(), formatter.parse("2009-12-01 01:00:00"));
//		
//		List<LighthouseEvent> listEventsFromQuery = new PullModel(model).executeQueryAfterCheckout(mapClassFqnToLastRevisionTimestamp);
//
//		boolean containEntityEvents = true;
//		containEntityEvents = containEntityEvents && listEventsFromQuery.contains(eventMainClass);
//		containEntityEvents = containEntityEvents && listEventsFromQuery.contains(eventfieldComittedBeforeRevisionTime);
//		containEntityEvents = containEntityEvents && listEventsFromQuery.contains(eventfieldCreatedAfterRevision); 
//		containEntityEvents = containEntityEvents && listEventsFromQuery.contains(eventFieldRemoveAndCommittedAfterRevision);
//		containEntityEvents = containEntityEvents && listEventsFromQuery.contains(eventFieldModifyWithoutAdd);
//		
//		boolean containRelEvents = true;
//		containRelEvents = containRelEvents && listEventsFromQuery.contains(eventfieldComittedBeforeRevisionTime);
//		containRelEvents = containRelEvents && listEventsFromQuery.contains(eventRelCreatedAfterRevision);
//		containRelEvents = containRelEvents && listEventsFromQuery.contains(eventRelNotInside);
//		containRelEvents = containRelEvents && listEventsFromQuery.contains(eventRelRemoveAndCommittedAfterRevision);
//		containRelEvents = containRelEvents && listEventsFromQuery.contains(eventRelModifyWithoutAdd);
//		
//		boolean result = ((listEventsFromQuery.size()==10) && containEntityEvents && containRelEvents);
//		assertEquals(true, result);
	}

}
