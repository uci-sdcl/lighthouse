package edu.uci.lighthouse.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import edu.uci.lighthouse.model.jpa.LHEventDAO;

public class BuildLHBaseFile {

	public static LighthouseFile execute(LighthouseModel lhModel, String fqnClazz, Date revisionTime, LighthouseAuthor author) {
		LighthouseFile lhFile = new LighthouseFile();
		LighthouseFileManager fileManager = new LighthouseFileManager(lhFile);
		LinkedHashSet<LighthouseEntity> listEntitiesInside = new LinkedHashSet<LighthouseEntity>();
		listEntitiesInside = selectEntitiesInside(listEntitiesInside,lhModel,fqnClazz);

		List<LighthouseEvent> listEvents = new LHEventDAO().executeQueryLhBaseFile(listEntitiesInside,revisionTime,author);

		for (LighthouseEvent event : listEvents) {
			if (event.getType()==LighthouseEvent.TYPE.ADD) {
				if (!wasEventRemoved(listEvents,event)) {
					Object artifact = event.getArtifact();
					if (artifact instanceof LighthouseRelationship) {
						if (!isValidRelationship(artifact, listEntitiesInside)) {
							continue; // do NOT add relationship
						}
					}
					fileManager.addArtifact(event.getArtifact());
				}
			}
		}
		return lhFile;
	}

	private static boolean isValidRelationship(Object artifact, LinkedHashSet<LighthouseEntity> listEntitiesInside) {
		boolean result = true;
		LighthouseRelationship rel = (LighthouseRelationship) artifact;
		if (!listEntitiesInside.contains(rel.getFromEntity())) {
			if (	rel.getType()==LighthouseRelationship.TYPE.CALL 
					|| rel.getType()==LighthouseRelationship.TYPE.USES
					|| rel.getType()==LighthouseRelationship.TYPE.HOLDS) {
				result = false;
			}
		}
		return result;
	}
	
	private static boolean wasEventRemoved(List<LighthouseEvent> listEvents, LighthouseEvent paramEvent) {
		for (LighthouseEvent event : listEvents) {
			if (event.getArtifact().equals(paramEvent.getArtifact())) {
				if (event.isCommitted()==true) {
					if (event.getType() == LighthouseEvent.TYPE.REMOVE) {
						return true;
					}					
				}
			}
		}
		return false;
	}

	/**Recursion
	 * 
	 * @param listEntitiesInside should be a new LinkedHashSet()
	 * 
	 * */ 
	private static LinkedHashSet<LighthouseEntity> selectEntitiesInside(LinkedHashSet<LighthouseEntity> listEntitiesInside, LighthouseModel lhModel, String fqnClazz) {
		LighthouseModelManager lhModelManager = new LighthouseModelManager(lhModel);
		List<LighthouseEntity> subListEntitiesInside = lhModelManager.getEntitiesInsideClass(fqnClazz);
		for (LighthouseEntity entity : subListEntitiesInside) {
			if (entity instanceof LighthouseClass || entity instanceof LighthouseInterface) { // That is a Inner class
				listEntitiesInside.addAll(selectEntitiesInside(listEntitiesInside, lhModel, entity.getFullyQualifiedName()));
			}
		}
		listEntitiesInside.addAll(subListEntitiesInside);
		listEntitiesInside.add(lhModelManager.getEntity(fqnClazz));
		return listEntitiesInside;
	}
	
}
