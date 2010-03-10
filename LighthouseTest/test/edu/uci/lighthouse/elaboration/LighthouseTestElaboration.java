package edu.uci.lighthouse.elaboration;

import java.io.IOException;

import junit.framework.TestCase;

import org.dom4j.DocumentException;

import edu.uci.lighthouse.model.jpa.JPAException;

public class LighthouseTestElaboration extends TestCase {

	public void testElaborateLHModelAndLHFileXML() throws DocumentException, IOException {
//		IWorkspace workspace = ResourcesPlugin.getWorkspace();
//
//		LinkedList<String> listProjects = new LinkedList<String>();
//		listProjects.add("AtmDeitelSample");
//
//		// Parse the whole ATM Project
//		LighthouseModelTest model = new LighthouseModelTest();
//		UtilModel.createModel(workspace, listProjects, model);
//
//		// Save LH Model in XML file
//		new LighthouseModelXMLPersistence(model).save(LHTestDataFiles.XML_MODEL);
//
//		// Save LH Model Into the database in order to help the
//		// BuilderRelationship to handle the External classes
//		new LighthouseModelManagerPersistence(model).saveAllIntoDataBase();
//
//		// Parse the ATM File
//		LighthouseFile fileModel = UtilModel.parseJustOneFile(workspace, listProjects, "ATM.java");
//		
//		// Save LHFile in XML file
//		new LighthouseFileXMLPersistence(fileModel).save(LHTestDataFiles.ATM_JAVA_MODIFIED);
	}

	public void testElaborateLHDeltaXML() throws DocumentException, IOException {
//		// Load old file
//		LighthouseFile fileATMclass = new LighthouseFile();
//		new LighthouseFileXMLPersistence(fileATMclass).load(LHTestDataFiles.XML_ATM_JAVA);
//		
//		// Load new file
//		LighthouseFile fileATMclassModified = new LighthouseFile();
//		new LighthouseFileXMLPersistence(fileATMclassModified).load(LHTestDataFiles.XML_ATM_JAVA_MODIFIED);
//		
//		// Generate delta
//		LighthouseDelta delta = new LighthouseDelta(fileATMclass, fileATMclassModified);
//		
//		// Save delta to xml file
//		new LHDeltaXMLPersistence(delta).save(LHTestDataFiles.XML_DELTA_ATM_CLASS);
	}
	
	public void testElaboratePushXML() throws DocumentException, IOException, JPAException {
//		LighthouseModelTest xmlModel = new LighthouseModelTest();
//		new LighthouseModelXMLPersistence(xmlModel).load(LHTestDataFiles.XML_LH_MODEL);
//		
//		// Save LH Model Into the database in order to help the
//		// BuilderRelationship to handle the External classes
//		new LighthouseModelManagerPersistence(xmlModel).saveAllIntoDataBase();
//		
//		// Load delta from xml file
//		LighthouseDelta xmlDelta = new LighthouseDelta();
//		new LHDeltaXMLPersistence(xmlDelta).load(LHTestDataFiles.XML_DELTA_ATM_CLASS);
//		
//		// Update Model
//		new PushModel(xmlModel).execute(xmlDelta);
//
//		// Save LH Model in XML file
//		new LighthouseModelXMLPersistence(xmlModel).save(LHTestDataFiles.XML_UPDATED_MODEL);
	}
	
	public void testElaboratePullXML() throws DocumentException, JPAException {
//		LighthouseModelTest xmlModel = new LighthouseModelTest();
//		new LighthouseModelXMLPersistence(xmlModel).load(LHTestDataFiles.XML_LH_MODEL);
//		
//		// Save LH Model Into the database in order to help the
//		// BuilderRelationship to handle the External classes
//		new LighthouseModelManagerPersistence(xmlModel).saveAllIntoDataBase();
//		
//		LighthouseModelTest pulledModel = new LighthouseModelTest();
//		
//		new PullModel(pulledModel).run();
	}
	
	public void testCreateFirstLHModel() throws JPAException {
//		IWorkspace workspace = ResourcesPlugin.getWorkspace();
//
//		LinkedList<String> listProjects = new LinkedList<String>();
//		listProjects.add("Atm");
//
//		// Parse the whole ATM Project
//		LighthouseModelTest model = new LighthouseModelTest();
//		UtilModel.createLHModel(workspace, listProjects, model);
//		
//		// Save LH Model Into the database
//		new LighthouseModelManagerPersistence(model).saveAllIntoDataBase();
	}
	
}
