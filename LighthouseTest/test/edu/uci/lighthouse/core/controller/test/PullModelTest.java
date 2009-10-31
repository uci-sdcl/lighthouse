package edu.uci.lighthouse.core.controller.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import junit.framework.TestCase;
import edu.uci.lighthouse.core.controller.PullModel;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseField;
import edu.uci.lighthouse.model.LighthouseModelManagerPersistence;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.jpa.JPAUtilityException;
import edu.uci.lighthouse.model.util.LHPreference;
import edu.uci.lighthouse.test.util.LighthouseModelTest;

public class PullModelTest extends TestCase {

	public void testExecuteQueryTimeout() throws ParseException, JPAUtilityException {
		LighthouseModelTest model = new LighthouseModelTest();
		LighthouseModelManagerPersistence lighthouseManager = new LighthouseModelManagerPersistence(model);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		LighthouseClass clazz_A = new LighthouseClass("Class A");
		LighthouseField field_A = new LighthouseField("Field A");
		LighthouseRelationship rel_A = new LighthouseRelationship(field_A,clazz_A,LighthouseRelationship.TYPE.INSIDE);
		
		LighthouseEvent eventClazz_A = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, clazz_A);
		eventClazz_A.setTimestamp(formatter.parse("2009-12-01 01:00:00"));
		lighthouseManager.addEvent(eventClazz_A);
		
		LighthouseEvent eventField_A = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, field_A);
		eventField_A.setTimestamp(formatter.parse("2009-12-01 01:30:00"));
		lighthouseManager.addEvent(eventField_A);
		
		LighthouseEvent eventRel_A = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, rel_A);
		eventRel_A.setTimestamp(formatter.parse("2009-12-01 02:00:00"));
		lighthouseManager.addEvent(eventRel_A);
		
		PullModel pullModel = new PullModel(model);
		List<LighthouseEvent> listEvent = pullModel.executeQueryTimeout(formatter.parse("2009-12-01 01:30:00"));
		
		assertEquals(true,listEvent.contains(eventField_A) && listEvent.contains(eventRel_A));
	}
	
	public void testExecuteQueryEventAfterCheckout() throws JPAUtilityException, ParseException {
		LighthouseModelTest model = new LighthouseModelTest();
		LighthouseModelManagerPersistence lighthouseManager = new LighthouseModelManagerPersistence(model);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		LighthouseClass clazz_A = new LighthouseClass("Class A");
		LighthouseField field_A = new LighthouseField("Field A");
		LighthouseField field_B = new LighthouseField("Field B");
		LighthouseField field_C = new LighthouseField("Field C");
		
		// It WILL show up in the result
		LighthouseEvent eventClazz_A = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, clazz_A);
		eventClazz_A.setTimestamp(formatter.parse("2009-12-01 01:00:00"));
		lighthouseManager.addEvent(eventClazz_A);
		
		// It will NOT show up in the result - TIME
		LighthouseEvent eventField_A = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, field_A);
		eventField_A.setTimestamp(formatter.parse("2009-12-01 00:50:00"));
		lighthouseManager.addEvent(eventField_A);
		
		// It WILL show up in the result
		LighthouseEvent eventField_A_MOdify = new LighthouseEvent(LighthouseEvent.TYPE.MODIFY, LHPreference.author, field_A);
		eventField_A_MOdify.setTimestamp(formatter.parse("2009-12-01 01:10:00"));
		lighthouseManager.addEvent(eventField_A_MOdify);
		
		// It will NOT show up in the result - TIME
		LighthouseEvent eventField_A_Old = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, field_A);
		eventField_A_Old.setTimestamp(formatter.parse("2009-12-01 00:30:00"));
		lighthouseManager.addEvent(eventField_A_Old);
		
		// It will NOT show up in the result NOT inside
		LighthouseEvent eventField_B = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, field_B);
		eventField_B.setTimestamp(formatter.parse("2009-12-01 01:20:00"));
		lighthouseManager.addEvent(eventField_B);
		
		// It WILL show up in the result
		LighthouseEvent eventField_C = new LighthouseEvent(LighthouseEvent.TYPE.REMOVE, LHPreference.author, field_C);
		eventField_C.setTimestamp(formatter.parse("2009-12-01 01:30:00"));
		lighthouseManager.addEvent(eventField_C);
		
		// It WILL show up in the result
		LighthouseEvent eventField_C_Modify = new LighthouseEvent(LighthouseEvent.TYPE.MODIFY, LHPreference.author, field_C);
		eventField_C_Modify.setTimestamp(formatter.parse("2009-12-01 01:31:00"));
		lighthouseManager.addEvent(eventField_C_Modify);
		
		LighthouseRelationship rel_A = new LighthouseRelationship(field_A,clazz_A,LighthouseRelationship.TYPE.INSIDE);
		LighthouseRelationship rel_B = new LighthouseRelationship(field_B,clazz_A,LighthouseRelationship.TYPE.USES); // not inside
		LighthouseRelationship rel_C = new LighthouseRelationship(field_C,clazz_A,LighthouseRelationship.TYPE.INSIDE);
		
		LighthouseEvent eventRel_A = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, rel_A);
		eventRel_A.setTimestamp(formatter.parse("2009-12-01 02:00:00"));
		lighthouseManager.addEvent(eventRel_A);
		
		LighthouseEvent eventRel_B = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, rel_B);
		eventRel_B.setTimestamp(formatter.parse("2009-12-01 02:10:00"));
		lighthouseManager.addEvent(eventRel_B);
		
		LighthouseEvent eventRel_C = new LighthouseEvent(LighthouseEvent.TYPE.ADD, LHPreference.author, rel_C);
		eventRel_C.setTimestamp(formatter.parse("2009-12-01 02:20:00"));
		lighthouseManager.addEvent(eventRel_C);
		
		model.setCheckedOutEntity(clazz_A.getFullyQualifiedName(), formatter.parse("2009-12-01 01:00:00"));
		
		List<LighthouseEvent> listEvents = new PullModel(model).executeQueryEventAfterCheckout();

		boolean result = (listEvents.size()==4) && listEvents.contains(eventClazz_A) && listEvents.contains(eventField_A_MOdify) && listEvents.contains(eventField_C) && listEvents.contains(eventField_C_Modify);
		assertEquals(true, result);
	}

}
