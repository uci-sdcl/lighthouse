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
import edu.uci.lighthouse.model.LighthouseFile;
import edu.uci.lighthouse.model.LighthouseFileManager;
import edu.uci.lighthouse.model.LighthouseRelationship;

public class LighthouseFileXMLPersistence extends AbstractXMLPersistence implements IPersistence {
	
	private static final String defaultFileName = "lighthouse-model.xml";

	private LighthouseFile model;

	public LighthouseFileXMLPersistence(LighthouseFile model) {
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
		XMLWriter writer = new XMLWriter(new FileWriter(fileName), OutputFormat
				.createPrettyPrint());
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
				new LighthouseFileManager(model).addArtifact(e);
			}
		}
		elements = root.element("relationships");
		if (elements!=null) {
			for (Iterator i = elements.elementIterator(); i.hasNext();) {
				Element relationship = (Element) i.next();
				LighthouseRelationship r = readRelationship(relationship);
				new LighthouseFileManager(model).addArtifact(r);
			}
		}
	}
	
}
