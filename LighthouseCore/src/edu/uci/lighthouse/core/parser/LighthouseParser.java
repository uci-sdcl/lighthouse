package edu.uci.lighthouse.core.parser;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

import edu.uci.ics.sourcerer.extractor.ast.FeatureExtractor;
import edu.uci.ics.sourcerer.util.io.Logging;
import edu.uci.ics.sourcerer.util.io.Property;
import edu.uci.ics.sourcerer.util.io.PropertyManager;
import edu.uci.lighthouse.model.LighthouseAbstractModel;
import edu.uci.lighthouse.model.jpa.JPAUtilityException;

public class LighthouseParser {

	private static final String UNRESOLVED = "_UNRESOLVED_.";
	
	private FeatureExtractor getFeatureExtractor() {
		PropertyManager properties = PropertyManager.getProperties(null);
		properties.setProperty(Property.ENTITY_WRITER, LighthouseEntityWriter.class.getName());
 	    properties.setProperty(Property.RELATION_WRITER, LighthouseRelationshipWriter.class.getName());
 	    
 	    IWorkspace workspace = ResourcesPlugin.getWorkspace();
 	    String outputPath = workspace.getRoot().getLocationURI().getPath();	    
 	    properties.setProperty(Property.OUTPUT, outputPath);
 	    
 	    FeatureExtractor extractor = new FeatureExtractor();
 	    Logging.initializeLogger();		
		extractor.setOutput(properties);
 	    
		return extractor;
	}

	public void execute(LighthouseAbstractModel model, Collection<IFile> files) throws JPAUtilityException {
		FeatureExtractor extractor = getFeatureExtractor();

		extractor.extractSourceFiles(files);
	    
		BuilderEntity.getInstance().populateAllEntityToModel(model);
		BuilderRelationship.getInstance().populateAllRelationshipsToModel(model);
	}

	public void execute(LighthouseAbstractModel model, IFile file) throws JPAUtilityException {
		execute(model,Collections.singleton(file));
	} 
	
	public void executeInAJob(final LighthouseAbstractModel model, final Collection<IFile> files, final IParserAction action) throws JPAUtilityException {
/*		class ParserJob extends Job {
			public ParserJob() {
				super(Messages.getString("LighthouseParser.job.msg"));
				addJobChangeListener(new JobChangeAdapter() {
					@Override
					public void done(IJobChangeEvent event) {
						action.doAction();
					}
				});
			}
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				execute(model, files);
				return Status.OK_STATUS;
			}
		}
		ParserJob job = new ParserJob();
		job.setPriority(Job.SHORT);
		job.schedule();*/
		
		
		execute(model, files); 
		action.doAction();
	}
	
	public void executeInAJob(final LighthouseAbstractModel model, final IFile file, final IParserAction action) throws JPAUtilityException {
		executeInAJob(model, Collections.singleton(file), action);
	}
	
	static boolean isEntityUnresolved(String fqn) {
		if (fqn.indexOf(LighthouseParser.UNRESOLVED) != -1) {
			return true;
		} else {
			return false;
		}		
	}
	
}
