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
import java.util.Iterator;

import javax.persistence.PersistenceException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseFile;
import edu.uci.lighthouse.model.LighthouseFileManager;
import edu.uci.lighthouse.model.LighthouseRelationship;

public class LighthouseFileXMLPersistence extends AbstractXMLPersistence
		implements IPersistence {

	private static final String defaultFileName = "lighthouse-model.xml";

	private LighthouseFile model;

	protected LighthouseFileXMLPersistence(LighthouseFile model) {
		this.model = model;
	}

	@Override
	public void save() throws PersistenceException {
		save(defaultFileName);
	}

	@Override
	public void save(String fileName) throws PersistenceException {
		try {
			Document document = DocumentHelper.createDocument();
			writeModel(document.addElement("model"));
			XMLWriter writer = new XMLWriter(new FileWriter(fileName),
					OutputFormat.createPrettyPrint());
			writer.write(document);
			writer.close();
		} catch (Exception e) {
			throw new PersistenceException(e);
		}

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
	}

	//
	@Override
	public void load() throws PersistenceException {
		load(defaultFileName);
	}

	@Override
	public void load(String fileName) throws PersistenceException {
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(new File(fileName));
			readModel(document.getRootElement());
		} catch (Exception e) {
			throw new PersistenceException(e);
		}

	}

	@SuppressWarnings("unchecked")
	private void readModel(Element root) {
		Element elements;
		elements = root.element("entities");
		if (elements != null) {
			for (Iterator i = elements.elementIterator(); i.hasNext();) {
				Element entity = (Element) i.next();
				LighthouseEntity e = readEntity(entity);
				new LighthouseFileManager(model).addArtifact(e);
			}
		}
		elements = root.element("relationships");
		if (elements != null) {
			for (Iterator i = elements.elementIterator(); i.hasNext();) {
				Element relationship = (Element) i.next();
				LighthouseRelationship r = readRelationship(relationship);
				new LighthouseFileManager(model).addArtifact(r);
			}
		}
	}

}
