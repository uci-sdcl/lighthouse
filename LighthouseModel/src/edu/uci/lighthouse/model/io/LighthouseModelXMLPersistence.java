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
package edu.uci.lighthouse.model.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseRelationship;

public class LighthouseModelXMLPersistence extends AbstractXMLPersistence implements IPersistence {

	private LighthouseModel model;

	private static final String defaultFileName = "lighthouse-model.xml";

	public LighthouseModelXMLPersistence(LighthouseModel model) {
		this.model = model;
	}

	@Override
	public void save() throws IOException {
		save(defaultFileName);
	}

	@Override
	public void save(String fileName) throws IOException {
		Document document = DocumentHelper.createDocument();
		writeModel(document.addElement("model"));
		XMLWriter writer = new XMLWriter(new FileWriter(fileName), OutputFormat.createPrettyPrint());
		writer.write(document);
		writer.close();
	}

	private void writeModel(Element root) {
		Element eleEntities = root.addElement("entities");
		for (LighthouseEntity entity : model.getEntities()) {
			writeEntity(entity, eleEntities);
		}
		Element eleRelationships = root.addElement("relationships");
		for (LighthouseRelationship relationship : model.getRelationships()) {
			writeRelationship(relationship, eleRelationships);
		}
		Element eleEvents = root.addElement("events");
		for (LighthouseEvent event : model.getListEvents()) {
			writeEvent(event, eleEvents);
		}
	}
	
	//
	@Override
	public void load() throws DocumentException {
		load(defaultFileName);
	}

	@Override
	public void load(String fileName) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(fileName));
		readModel(document.getRootElement());
	}
	
	@SuppressWarnings("unchecked")
	private void readModel(Element root) {
		Element elements;
		elements = root.element("entities");
		if (elements!=null) {
			for (Iterator i = elements.elementIterator(); i.hasNext();) {
				Element entity = (Element) i.next();
				LighthouseEntity e = readEntity(entity);
				new LighthouseModelManager(model).addArtifact(e);
			}
		}
		elements = root.element("relationships");
		if (elements!=null) {
			for (Iterator i = elements.elementIterator(); i.hasNext();) {
				Element relationship = (Element) i.next();
				LighthouseRelationship r = readRelationship(relationship);
				new LighthouseModelManager(model).addArtifact(r);
			}
		}
		elements = root.element("events");
		if (elements!=null) {
			for (Iterator i = elements.elementIterator(); i.hasNext();) {
				Element element = (Element) i.next();
				LighthouseEvent event = readEvent(element); 
				new LighthouseModelManager(model).addEvent(event);
			}
		}
	}

}
