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

import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.jpa.JPAUtilityException;

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
		Element entities = root.addElement("events");
		for (LighthouseEvent event : model.getListEvents()) {
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
		loadModel(document.getRootElement());
	}
	
	@SuppressWarnings("unchecked")
	private void loadModel(Element root) {
		Element elements;
		elements = root.element("events");
		for (Iterator i = elements.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			LighthouseEvent event = readEvent(element); 
			new LighthouseModelManager(model).addEvent(event);
		}
	}

}
