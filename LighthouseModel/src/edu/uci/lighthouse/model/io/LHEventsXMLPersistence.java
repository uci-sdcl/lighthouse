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
import java.util.LinkedHashSet;

import javax.persistence.PersistenceException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import edu.uci.lighthouse.model.LighthouseEvent;

public class LHEventsXMLPersistence extends AbstractXMLPersistence implements
		IPersistence {

	/** Events that was not saved in the database due to connection error */
	private LinkedHashSet<LighthouseEvent> listPendingEvents = new LinkedHashSet<LighthouseEvent>();

	private static final String defaultFileName = "lighthouse-pending-events.xml";

	protected LHEventsXMLPersistence(
			LinkedHashSet<LighthouseEvent> listPendingEvents) {
		this.listPendingEvents = listPendingEvents;
	}

	@Override
	public void save() throws PersistenceException {
		save(defaultFileName);
	}

	@Override
	public void save(String fileName) throws PersistenceException {
		try {
			Document document = DocumentHelper.createDocument();
			writeDelta(document.addElement("root"));
			XMLWriter writer = new XMLWriter(new FileWriter(fileName),
					OutputFormat.createPrettyPrint());
			writer.write(document);
			writer.close();
		} catch (Exception e) {
			throw new PersistenceException(e);
		}
	}

	private void writeDelta(Element root) {
		Element entities = root.addElement("events");
		for (LighthouseEvent event : listPendingEvents) {
			writeEvent(event, entities);
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
		elements = root.element("events");
		for (Iterator i = elements.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			LighthouseEvent event = readEvent(element);
			listPendingEvents.add(event);
		}
	}

}
