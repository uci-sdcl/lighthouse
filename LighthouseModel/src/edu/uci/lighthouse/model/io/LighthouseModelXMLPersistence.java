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
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseRelationship;

public class LighthouseModelXMLPersistence extends AbstractXMLPersistence implements IPersistence {

	private LighthouseModel model;

	private static final String defaultFileName = "lighthouse-model.xml";

	protected LighthouseModelXMLPersistence(LighthouseModel model) {
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
		Element eleEvents = root.addElement("events");
		for (LighthouseEvent event : model.getListEvents()) {
			writeEvent(event, eleEvents);
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
