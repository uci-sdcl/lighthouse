package edu.uci.lighthouse.model;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;

import edu.uci.lighthouse.model.LighthouseEvent.TYPE;

public class BuildLHBaseFile {

	public static LighthouseFile execute(LighthouseModel lhModel, String fqnClazz, Date revisionTime, LighthouseAuthor author) {
		LighthouseFile lhFile = new LighthouseFile();
		LighthouseFileManager fileManager = new LighthouseFileManager(lhFile);
		LighthouseEntity clazz = lhModel.getEntity(fqnClazz);
		LinkedHashSet<LighthouseEntity> listEntitiesInside = new LinkedHashSet<LighthouseEntity>();
		listEntitiesInside.add(clazz);
		Collection<LighthouseRelationship> listRel = lhModel.getRelationshipsTo(clazz);
		for (LighthouseRelationship rel : listRel) {
			// get all entities inside the fqnClazz
			if (rel.getType()==LighthouseRelationship.TYPE.INSIDE) {
				LighthouseEntity entityInside = rel.getFromEntity();
				listEntitiesInside.add(entityInside);
				if (isInnerClass(entityInside)) {
					// I need to create just one method with recurssion or use a stack
					Collection<LighthouseRelationship> listInnerRel = lhModel.getRelationshipsTo(entityInside);
					for (LighthouseRelationship relInner : listInnerRel) {
						// get all entities inside the innerClazz
						if (relInner.getType()==LighthouseRelationship.TYPE.INSIDE) {
							LighthouseEntity innerEntityInside = relInner.getFromEntity();
							listEntitiesInside.add(innerEntityInside);
						}
					}
				}
			}
		}
		// for each entity inside
		for (LighthouseEntity entity : listEntitiesInside) {
			Collection<LighthouseEvent> listEntityEvents = lhModel.getEvents(entity);
			boolean shouldAddEntity = false;
			for (LighthouseEvent event : listEntityEvents) {
				if (event.isCommitted()) {
					if (event.getType() == TYPE.ADD) {
						shouldAddEntity = true;
					} else if (event.getType() == TYPE.REMOVE) {
						shouldAddEntity = false;
						break;
					}
				} 
//				else { // if is NOT committed
//					// check if the author create a new entity in his own working copy after the revision time
//					if (event.getAuthor().equals(author)) {
//						if (revisionTime.before(event.getTimestamp())) {
//							if (event.getType() == TYPE.ADD) {
//								shouldAddEntity = true;
//							} else if (event.getType() == TYPE.REMOVE) {
//								shouldAddEntity = false;
//								break;
//							}
//						}
//					}
//				}
			} // end-for-events
			if (shouldAddEntity) {
				fileManager.addEntity(entity);
				Collection<LighthouseRelationship> listRelFrom = lhModel.getRelationshipsFrom(entity);
				Collection<LighthouseRelationship> listRelTo = lhModel.getRelationshipsTo(entity);
				
				for (LighthouseRelationship relationship : listRelFrom) {
					Collection<LighthouseEvent> listRelEvents = lhModel.getEvents(relationship);
					boolean shouldAddRel = false;
					for (LighthouseEvent event : listRelEvents) {
						if (event.isCommitted()) {
							if (event.getType() == TYPE.ADD) {
								shouldAddRel = true;
							} else if (event.getType() == TYPE.REMOVE) {
								shouldAddRel = false;
								break;
							}
						} 
//						else { // if is NOT committed
//							// check if the author create a new entity in his own working copy after the revision time
//							if (event.getAuthor().equals(author)) {
//								if (revisionTime.before(event.getTimestamp())) {
//									if (event.getType() == TYPE.ADD) {
//										shouldAddEntity = true;
//									} else if (event.getType() == TYPE.REMOVE) {
//										shouldAddEntity = false;
//										break;
//									}
//								}
//							}
//						}
					}
					if (shouldAddRel) {
						fileManager.addRelationship(relationship);
					}
				}
				for (LighthouseRelationship relationship : listRelTo) {
					boolean shouldAddRel = false;
					if (!listEntitiesInside.contains(relationship.getFromEntity())) {
						if (	relationship.getType()==LighthouseRelationship.TYPE.CALL 
								|| relationship.getType()==LighthouseRelationship.TYPE.USES
								|| relationship.getType()==LighthouseRelationship.TYPE.HOLDS) {
							shouldAddRel = false;
							break;
						}
					}
					Collection<LighthouseEvent> listRelEvents = lhModel.getEvents(relationship);
					for (LighthouseEvent event : listRelEvents) {
						if (event.isCommitted()) {
							if (event.getType() == TYPE.ADD) {
								shouldAddRel = true;
							} else if (event.getType() == TYPE.REMOVE) {
								shouldAddRel = false;
								break;
							}
						} 
//						else { // if is NOT committed
//							// check if the author create a new entity in his own working copy after the revision time
//							if (event.getAuthor().equals(author)) {
//								if (revisionTime.before(event.getTimestamp())) {
//									if (event.getType() == TYPE.ADD) {
//										shouldAddEntity = true;
//									} else if (event.getType() == TYPE.REMOVE) {
//										shouldAddEntity = false;
//										break;
//									}
//								}
//							}
//						}
					}
					if (shouldAddRel) {
						fileManager.addRelationship(relationship);
					}
				}
			} // end-if-shouldAdd
		}
		return lhFile;
	}
	
	private static boolean isInnerClass(LighthouseEntity entity) {
		return (entity.getFullyQualifiedName().indexOf("$")!=-1);
	}
	
}
