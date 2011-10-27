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

import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseEvent;

public class LHDeltaXMLPersistence extends AbstractXMLPersistence implements IPersistence{

	private LighthouseDelta delta;
	
	private static final String defaultFileName = "lighthouse-delta.xml";
	
	public LHDeltaXMLPersistence(LighthouseDelta delta){
		this.delta = delta;
	}
	
	@Override
	public void save() throws IOException {
		save(defaultFileName);
	}

	@Override
	public void save(String fileName) throws IOException {
		Document document = DocumentHelper.createDocument();
		writeDelta(document.addElement("delta"));
		XMLWriter writer = new XMLWriter(new FileWriter(fileName), OutputFormat.createPrettyPrint());
		writer.write(document);
		writer.close();
	}

	private void writeDelta(Element root) {
		Element entities = root.addElement("events");
		for (LighthouseEvent event : delta.getEvents()) {
			writeEvent(event, entities);
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
		elements = root.element("events");
		for (Iterator i = elements.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			LighthouseEvent event = readEvent(element); 
			delta.addEvent(event);
		}
	}
	
}
