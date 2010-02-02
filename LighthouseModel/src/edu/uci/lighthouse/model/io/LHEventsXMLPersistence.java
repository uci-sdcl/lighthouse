package edu.uci.lighthouse.model.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import edu.uci.lighthouse.model.LighthouseEvent;

public class LHEventsXMLPersistence extends AbstractXMLPersistence implements IPersistence{

	/** Events that was not saved in the database due to connection error */
	private LinkedHashSet<LighthouseEvent> listPendingEvents = new LinkedHashSet<LighthouseEvent>();
	
	private static final String defaultFileName = "lighthouse-pending-events.xml";
	
	public LHEventsXMLPersistence(LinkedHashSet<LighthouseEvent> listPendingEvents){
		this.listPendingEvents = listPendingEvents;
	}
	
	@Override
	public void save() throws IOException {
		save(defaultFileName);
	}

	@Override
	public void save(String fileName) throws IOException {
		Document document = DocumentHelper.createDocument();
		writeDelta(document.addElement("root"));
		XMLWriter writer = new XMLWriter(new FileWriter(fileName), OutputFormat.createPrettyPrint());
		writer.write(document);
		writer.close();
	}

	private void writeDelta(Element root) {
		Element entities = root.addElement("events");
		for (LighthouseEvent event : listPendingEvents) {
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
			listPendingEvents.add(event);
		}
	}
	
}
