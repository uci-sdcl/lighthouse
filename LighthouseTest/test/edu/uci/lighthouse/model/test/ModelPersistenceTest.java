package edu.uci.lighthouse.model.test;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;

import junit.framework.TestCase;

import org.dom4j.DocumentException;

import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.io.IPersistence;
import edu.uci.lighthouse.model.io.LighthouseModelXMLPersistence;
import edu.uci.lighthouse.model.jpa.LHEventDAO;
import edu.uci.lighthouse.test.util.LighthouseModelTest;

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
