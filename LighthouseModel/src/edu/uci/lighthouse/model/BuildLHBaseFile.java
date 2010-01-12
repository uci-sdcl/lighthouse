package edu.uci.lighthouse.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.LHEventDAO;

public class BuildLHBaseFile {

	public static LighthouseFile execute(LighthouseModel lhModel, String fqnClazz, Date revisionTime, LighthouseAuthor author) throws JPAException {
		LighthouseFile lhFile = new LighthouseFile();
		LighthouseFileManager fileManager = new LighthouseFileManager(lhFile);
		LinkedHashSet<LighthouseEntity> listEntitiesInside = new LighthouseModelManager(lhModel).selectEntitiesInsideClass(fqnClazz);
		List<LighthouseEvent> listEvents = new LHEventDAO().executeQueryLhBaseFile(listEntitiesInside,revisionTime,author);
		if (listEvents!=null && listEvents.size()!=0) {
			for (LighthouseEvent event : listEvents) {
				if (event.getType()==LighthouseEvent.TYPE.ADD) {
					if (!LighthouseModelUtil.wasEventRemoved(listEvents,event,author)) {
						Object artifact = event.getArtifact();
						if (artifact instanceof LighthouseRelationship) {
							if (!LighthouseModelUtil.isValidRelationship((LighthouseRelationship) artifact, listEntitiesInside)) {
								continue; // do NOT add relationship
							}
						}
						fileManager.addArtifact(event.getArtifact());
					}
				}
			}
		}
		return lhFile;
	}

}
