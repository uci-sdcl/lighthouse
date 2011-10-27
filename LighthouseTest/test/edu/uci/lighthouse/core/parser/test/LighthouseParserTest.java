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
package edu.uci.lighthouse.core.parser.test;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.dom4j.DocumentException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import edu.uci.lighthouse.core.parser.LighthouseParser;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseFile;
import edu.uci.lighthouse.model.LighthouseFileManager;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.io.LighthouseFileXMLPersistence;
import edu.uci.lighthouse.model.io.LighthouseModelXMLPersistence;
import edu.uci.lighthouse.model.jpa.JPAUtility;
import edu.uci.lighthouse.model.jpa.JPAUtilityException;
import edu.uci.lighthouse.model.util.LHPreference;
import edu.uci.lighthouse.parser.ParserException;
import edu.uci.lighthouse.test.util.LHTestDataFiles;
import edu.uci.lighthouse.test.util.LighthouseModelTest;

public class LighthouseParserTest extends TestCase {

	public void testExecuteLighthouseAbstractModelCollectionOfIFile() throws DocumentException, IOException, JPAUtilityException, ParserException {
		Map<String, String> mapDatabaseProperties = new HashMap<String, String>();
		mapDatabaseProperties.put("hibernate.hbm2ddl.auto", "create-drop");
		JPAUtility.createEntityManager(mapDatabaseProperties);
		
		LighthouseModelTest xmlModel = new LighthouseModelTest();
		new LighthouseModelXMLPersistence(xmlModel).load(LHTestDataFiles.XML_LH_MODEL);
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();		
		LinkedList<String> listProjects = new LinkedList<String>();
		listProjects.add("LighthouseModel");

		LighthouseModelTest currentModel = new LighthouseModelTest();
		importEclipseProjectToDatabase(workspace, listProjects, currentModel);
		
		Set<LighthouseEvent> listXMLEvents = xmlModel.getListEvents();
		Set<LighthouseEvent> listCurrrentEvents = currentModel.getListEvents();

		boolean sameSize = listXMLEvents.size()==listCurrrentEvents.size();
		boolean contain = listXMLEvents.containsAll(listCurrrentEvents);
		assertEquals(true, sameSize&&contain);
	}
	
	public void testParseJustOneFile() throws DocumentException, IOException, JPAUtilityException, ParserException {
		Map<String, String> mapDatabaseProperties = new HashMap<String, String>();
		mapDatabaseProperties.put("hibernate.hbm2ddl.auto", "create-drop");
		JPAUtility.createEntityManager(mapDatabaseProperties);
		
		LighthouseModel lhModel = LighthouseModel.getInstance();
		new LighthouseModelXMLPersistence(lhModel).load(LHTestDataFiles.XML_LH_MODEL);
		
		// Save LH Model Into the database in order to help the
		// BuilderRelationship to handle the External classes
		new LighthouseModelManager(lhModel).saveEventsIntoDatabase(lhModel.getListEvents());
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		LinkedList<String> listProjects = new LinkedList<String>();
		listProjects.add("AtmDeitelSample");
		// Parse the ATM File
		LighthouseFile currentFileModel = parseJustOneFile(workspace, listProjects, "ATM.java");

		// Load LHFile in XML file
		LighthouseFile xmlFile = new LighthouseFile();
		new LighthouseFileXMLPersistence(xmlFile).load(LHTestDataFiles.XML_ATM_JAVA);
 
		LighthouseDelta delta = new LighthouseDelta(LHPreference.author, currentFileModel, xmlFile);
		assertEquals(0, delta.getEvents().size());
	}

	public void testImport2Projetcs() throws DocumentException, ParserException, JPAUtilityException {

//		Map<String, String> mapDatabaseProperties = new HashMap<String, String>();
//		mapDatabaseProperties.put("hibernate.hbm2ddl.auto", "create-drop");
//		JPAUtility.createEntityManager(mapDatabaseProperties);
//		
//		IWorkspace workspace = ResourcesPlugin.getWorkspace();		
//		LinkedList<String> listProjects = new LinkedList<String>();
//		listProjects.add("LighthouseModel");
//		listProjects.add("LighthouseJavaParser");
//		listProjects.add("LighthouseParser");
//		listProjects.add("LighthouseCore");
//		listProjects.add("LighthouseTest");
//
//		LighthouseModelTest model = new LighthouseModelTest();
//		importEclipseProjectToDatabase(workspace, listProjects, model);
//		
//		System.out.println("number of events: " + model.getListEvents().size());
//		System.out.println("end");
				
	}
	
	private void importEclipseProjectToDatabase(IWorkspace workspace, Collection<String> listEclipseProject, LighthouseModel model) throws ParserException, JPAUtilityException {
		IProject[] projects = workspace.getRoot().getProjects();
		final Collection<IFile> files = new LinkedList<IFile>();
		for (IProject project : projects) {
			if (listEclipseProject.contains(project.getName())) {
				if (project.isOpen()) {
					files.addAll(getFilesFromProject(project));
				}
			}
		}
		if (files.size() > 0) {
			LighthouseParser parser = new LighthouseParser();
			parser.execute(files);
			Collection<LighthouseEntity> listEntities = parser.getListEntities();
			Collection<LighthouseRelationship> listLighthouseRel = parser.getListRelationships();
			LighthouseModelManager modelManager = new LighthouseModelManager(model);
			Collection<LighthouseEvent> listEvents = modelManager.createEventsAndSaveInModel(new LighthouseAuthor("Max"), listEntities, listLighthouseRel);
			modelManager.saveEventsIntoDatabase(listEvents);
		}
	}
	
	private Collection<IFile> getFilesFromProject(IProject project) {
		final Collection<IFile> files = new HashSet<IFile>();
		try {
			project.getFolder("src").accept(new IResourceVisitor() {
				@Override
				public boolean visit(IResource resource) throws CoreException {
					if (resource.getType() == IResource.FILE
							&& resource.getFileExtension().equalsIgnoreCase(
									"java")) {
						files.add((IFile) resource);
						return false;
					} else {
						return true;
					}
				}
			});
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return files;
	}
	
	private LighthouseFile parseJustOneFile(final IWorkspace workspace, Collection<String> listProjects, String fileName) throws JPAUtilityException, ParserException {
		IProject[] projects = workspace.getRoot().getProjects();
		for (int i = 0; i < projects.length; i++) {
			if (listProjects.contains(projects[i].getName())) {
				if (projects[i].isOpen()) {
					Collection<IFile> files = getFilesFromProject(projects[i]);
					for (IFile file : files) {
						if (file.getName().equals(fileName)) {
							LighthouseFile lighthouseFile = new LighthouseFile();
							LighthouseParser parser = new LighthouseParser();
							parser.execute(Collections.singleton(file));
							new LighthouseFileManager(lighthouseFile).buildLHFile(parser.getListEntities(), parser.getListRelationships());
							return lighthouseFile;
						}
					}
				}
			}
		} // end-of-for
		return null;
	}
	
}
