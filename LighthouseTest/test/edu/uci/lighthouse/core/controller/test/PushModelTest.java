package edu.uci.lighthouse.core.controller.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.dom4j.DocumentException;

import edu.uci.lighthouse.core.controller.PushModel;
import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModelManagerPersistence;
import edu.uci.lighthouse.model.io.LHDeltaXMLPersistence;
import edu.uci.lighthouse.model.io.LighthouseModelXMLPersistence;
import edu.uci.lighthouse.model.jpa.JPAUtilityException;
import edu.uci.lighthouse.model.jpa.LHEventDAO;
import edu.uci.lighthouse.test.util.LHTestDataFiles;
import edu.uci.lighthouse.test.util.LighthouseModelTest;

public class PushModelTest extends TestCase {

	public void testUpdateModelFromDelta() throws DocumentException, IOException, JPAUtilityException {
		LighthouseModelTest xmlModel = new LighthouseModelTest();
		new LighthouseModelXMLPersistence(xmlModel).load(LHTestDataFiles.XML_LH_MODEL);
		
		// Save LH Model Into the database in order to help the
		// BuilderRelationship to handle the External classes
		new LighthouseModelManagerPersistence(xmlModel).saveAllIntoDataBase();
		
		// Load delta from xml file
		LighthouseDelta xmlDelta = new LighthouseDelta();
		new LHDeltaXMLPersistence(xmlDelta).load(LHTestDataFiles.XML_DELTA_ATM_CLASS);
		
		// Update Model
		new PushModel(xmlModel).updateModelFromDelta(xmlDelta);
		
		// Load LH Updated Model in XML file
		LighthouseModelTest xmlUpdatedModel = new LighthouseModelTest();
		new LighthouseModelXMLPersistence(xmlUpdatedModel).load(LHTestDataFiles.XML_UPDATED_MODEL);
		
		assertEquals(true, xmlModel.getListEvents().containsAll(xmlUpdatedModel.getListEvents()));
	}
	
	public void testUpdateCommittedEvents() throws DocumentException, JPAUtilityException {
		LighthouseModelTest xmlModel = new LighthouseModelTest();
		new LighthouseModelXMLPersistence(xmlModel).load(LHTestDataFiles.XML_LH_MODEL);
		
		// Save LH Model Into the database in order to help the
		// BuilderRelationship to handle the External classes
		new LighthouseModelManagerPersistence(xmlModel).saveAllIntoDataBase();
		
		List<String> listClazzFqn = new ArrayList<String>();
		listClazzFqn.add("edu.prenticehall.deitel.Screen");
		listClazzFqn.add("edu.prenticehall.deitel.Transaction");
		new PushModel(xmlModel).updateCommittedEvents(listClazzFqn, "Max");
		
		String command = "SELECT e FROM LighthouseEvent e WHERE e.committed=1";
		List<LighthouseEvent> listEvents = new LHEventDAO().executeDynamicQuery(command);
		assertEquals(true, listEvents.size()==12);
	}

}
