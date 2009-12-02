package edu.uci.lighthouse.parser.java;

import java.util.Collection;

import org.eclipse.core.resources.IFile;

import edu.uci.ics.sourcerer.extractor.ast.FeatureExtractor;
import edu.uci.ics.sourcerer.util.io.Logging;
import edu.uci.ics.sourcerer.util.io.Property;
import edu.uci.ics.sourcerer.util.io.PropertyManager;
import edu.uci.lighthouse.parser.IParser;
import edu.uci.lighthouse.parser.ParserEntity;
import edu.uci.lighthouse.parser.ParserException;
import edu.uci.lighthouse.parser.ParserRelationship;

public class JavaParser implements IParser {

	@Override
	public Collection<ParserEntity> getEntities() {
		return SourcererOutput.getInstance().getEntities();
	}

	@Override
	public Collection<ParserRelationship> getRelationships() {
		return SourcererOutput.getInstance().getRelationships();
	}

	@Override
	public void parse(Collection<IFile> files) throws ParserException {
		FeatureExtractor extractor = getFeatureExtractor();
		extractor.extractSourceFiles(files);
	}

	private FeatureExtractor getFeatureExtractor() {
		PropertyManager properties = PropertyManager.getProperties(null);
		properties.setProperty(Property.ENTITY_WRITER, SourcererEntityWriter.class.getName());
 	    properties.setProperty(Property.RELATION_WRITER, SourcererRelationshipWriter.class.getName());
 	    
 	    String outputPath = "sourcerer.txt";
 	    properties.setProperty(Property.OUTPUT, outputPath);
 	    
 	    FeatureExtractor extractor = new FeatureExtractor();
 	    Logging.initializeLogger();		
		extractor.setOutput(properties);
 	    
		return extractor;
	}
	
}
