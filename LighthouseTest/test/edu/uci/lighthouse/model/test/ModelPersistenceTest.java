package edu.uci.lighthouse.model.test;

import junit.framework.TestCase;

public class ModelPersistenceTest extends TestCase {

//	public void testPersistence() throws IOException, DocumentException {
//		List<LighthouseEvent> listEvents = new LHEventDAO().list();
//		LighthouseModelTest model1 = new LighthouseModelTest();
//		LighthouseModelManager modelManager = new LighthouseModelManager(model1);
//		for (LighthouseEvent event : listEvents) {
//			modelManager.addEvent(event);
//		}
//		IPersistence mos = new LighthouseModelXMLPersistence(model1);
//		mos.save();
//		
//		LighthouseModelTest model2 = new LighthouseModelTest();
//		mos = new LighthouseModelXMLPersistence(model2);
//		mos.load();
//
//		assertEquals(model1.getEntities().size(),model2.getEntities().size());
//		assertEquals(model1.getRelationships().size(),model2.getRelationships().size());
//		
//		LinkedHashSet<LighthouseEvent> listEvents2 = model2.getListEvents();
//		LinkedHashSet<LighthouseEvent> listEvents1 = model1.getListEvents();
//		
//		assertEquals(listEvents1.size(),listEvents2.size());
//		
//		for (LighthouseEvent event2 : listEvents2) {
//			assertTrue(listEvents1.contains(event2));
//		}
//		
//	}
//	
}
