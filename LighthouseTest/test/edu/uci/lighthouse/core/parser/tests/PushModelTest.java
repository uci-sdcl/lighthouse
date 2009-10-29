package edu.uci.lighthouse.core.parser.tests;

import java.io.IOException;

import junit.framework.TestCase;

import org.dom4j.DocumentException;

import edu.uci.lighthouse.core.controler.PushModel;
import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseModelManagerPersistence;
import edu.uci.lighthouse.model.io.LHDeltaXMLPersistence;
import edu.uci.lighthouse.model.io.LighthouseModelXMLPersistence;
import edu.uci.lighthouse.model.jpa.JPAUtilityException;
import edu.uci.lighthouse.test.util.LHTestDataFiles;
import edu.uci.lighthouse.test.util.LighthouseModelTest;

public class PushModelTest extends TestCase {

	public void testExecute() throws DocumentException, IOException, JPAUtilityException {
		LighthouseModelTest xmlModel = new LighthouseModelTest();
		new LighthouseModelXMLPersistence(xmlModel).load(LHTestDataFiles.XML_LH_MODEL);
		
		// Save LH Model Into the database in order to help the
		// BuilderRelationship to handle the External classes
		new LighthouseModelManagerPersistence(xmlModel).saveAllIntoDataBase();
		
		// Load delta from xml file
		LighthouseDelta xmlDelta = new LighthouseDelta();
		new LHDeltaXMLPersistence(xmlDelta).load(LHTestDataFiles.XML_DELTA_ATM_CLASS);
		
		// Update Model
		new PushModel(xmlModel).execute(xmlDelta);
		
		// Load LH Updated Model in XML file
		LighthouseModelTest xmlUpdatedModel = new LighthouseModelTest();
		new LighthouseModelXMLPersistence(xmlUpdatedModel).load(LHTestDataFiles.XML_UPDATED_MODEL);
		
		assertEquals(true, xmlModel.getListEvents().containsAll(xmlUpdatedModel.getListEvents()));
	}

}
