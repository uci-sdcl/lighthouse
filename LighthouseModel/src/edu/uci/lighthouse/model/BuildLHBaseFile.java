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
package edu.uci.lighthouse.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.LHEventDAO;

public class BuildLHBaseFile {

	public static LighthouseFile execute(LighthouseModel lhModel, String fqnClazz, Date revisionTime, LighthouseAuthor author) throws JPAException {
		LighthouseFile lhFile = new LighthouseFile();
		LighthouseModelManager modelManager = new LighthouseModelManager(lhModel);
		LighthouseFileManager fileManager = new LighthouseFileManager(lhFile);
		
		HashMap<LighthouseClass, Collection<LighthouseEntity>> map = modelManager.selectEntitiesInsideClass(fqnClazz);

		for (Entry<LighthouseClass, Collection<LighthouseEntity>> entryEntitesInside : map.entrySet()) {
			Collection<LighthouseEntity> listEntitiesInside = entryEntitesInside.getValue();
			listEntitiesInside.add(entryEntitesInside.getKey());
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
		}
		return lhFile;
	}

}
