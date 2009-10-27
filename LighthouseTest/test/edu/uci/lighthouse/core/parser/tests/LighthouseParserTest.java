package edu.uci.lighthouse.core.parser.tests;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Set;

import junit.framework.TestCase;

import org.dom4j.DocumentException;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseFile;
import edu.uci.lighthouse.model.LighthouseModelManagerPersistence;
import edu.uci.lighthouse.model.io.LighthouseFileXMLPersistence;
import edu.uci.lighthouse.model.io.LighthouseModelXMLPersistence;
import edu.uci.lighthouse.test.util.LHTestDataFiles;
import edu.uci.lighthouse.test.util.LighthouseModelTest;
import edu.uci.lighthouse.test.util.UtilModel;

public class LighthouseParserTest extends TestCase {

	public void testExecuteLighthouseAbstractModelCollectionOfIFile() throws DocumentException, IOException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();		
		
		LighthouseModelTest xmlModel = new LighthouseModelTest();
		new LighthouseModelXMLPersistence(xmlModel).load(LHTestDataFiles.XML_LH_MODEL);
		
		LinkedList<String> listProjects = new LinkedList<String>();
		listProjects.add("AtmDeitelSample");

		LighthouseModelTest currentModel = new LighthouseModelTest();
		UtilModel.createModel(workspace, listProjects, currentModel);
		
		Set<LighthouseEvent> listXMLEvents = xmlModel.getListEvents();
		Set<LighthouseEvent> listCurrrentEvents = currentModel.getListEvents();

		boolean sameSize = listXMLEvents.size()==listCurrrentEvents.size();
		boolean contain = listXMLEvents.containsAll(listCurrrentEvents);
		
		assertEquals(true, sameSize&&contain);
	}
	
	
	public void testParseJustOneFile() throws DocumentException, IOException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
				
		LinkedList<String> listProjects = new LinkedList<String>();
		listProjects.add("AtmDeitelSample");
		
		// Parse the whole ATM Project
		LighthouseModelTest currentModel = new LighthouseModelTest();
		UtilModel.createModel(workspace, listProjects, currentModel);

		// Save LH Model Into the database in order to help the
		// BuilderRelationship to handle the External classes
		new LighthouseModelManagerPersistence(currentModel).saveAllIntoDataBase();

		// Parse the ATM File
		LighthouseFile currentFileModel = UtilModel.parseJustOneFile(workspace, listProjects, "ATM.java");
		
		// Load LHFile in XML file
		LighthouseFile xmlFile = new LighthouseFile();
		new LighthouseFileXMLPersistence(xmlFile).load(LHTestDataFiles.XML_ATM_JAVA);

		LighthouseDelta delta = new LighthouseDelta(currentFileModel, xmlFile);
		assertEquals(true, (delta.getEvents().size()==0));
	}
	
}
