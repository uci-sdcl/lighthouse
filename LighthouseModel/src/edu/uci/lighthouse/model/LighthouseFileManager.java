package edu.uci.lighthouse.model;

import java.util.Collection;

import edu.uci.lighthouse.model.LighthouseRelationship.TYPE;

/**
 *	This class is responsible to manipulate the LighthouseFile
 */
public class LighthouseFileManager {
	
	private LighthouseFile lighthouseFile;
	private LighthouseModel lighthouseModel = LighthouseModel.getInstance();

	public LighthouseFileManager(LighthouseFile lighthouseFileModel) {
		this.lighthouseFile = lighthouseFileModel; 
	}

	private LighthouseEntity addEntity(LighthouseEntity newEntity){
		LighthouseEntity entity = lighthouseModel.getEntity(newEntity.getFullyQualifiedName());
		if (entity == null) {
			entity = newEntity;
		}
		lighthouseFile.addEntity(entity);
		return entity;
	}
	
	private LighthouseRelationship addRelationship(LighthouseRelationship newRelationship){
		LighthouseRelationship relationship = lighthouseModel.getRelationship(newRelationship);
		if (relationship == null) {
			relationship = newRelationship;
		}
		handleEntitiesNotInsideClass(relationship);
		lighthouseFile.addRelationship(relationship);
		return newRelationship;
	}

	/** Need this method for add the Entities: ExternalClass and Modifiers in the LHBaseFile,
	 * because they have not the TYPE.INSIDE relationship */
	private void handleEntitiesNotInsideClass(
			LighthouseRelationship relationship) {
		LighthouseEntity fromEntity = relationship.getFromEntity();
		LighthouseEntity toEntity = relationship.getToEntity();
		if (fromEntity instanceof LighthouseExternalClass) {
			addEntity(fromEntity);
		}
		if (toEntity instanceof LighthouseExternalClass) {
			addEntity(toEntity);
		}
		if (relationship.getType()==TYPE.MODIFIED_BY) {
			addEntity(toEntity);
		}
	}

	/**
	 * @param artifact
	 * 		{@link LighthouseEntity}
	 * 		OR
	 * 		{@link LighthouseRelationship}
	 * */
	public Object addArtifact(Object artifact) {
		if (artifact instanceof LighthouseEntity) {
			LighthouseEntity entity = (LighthouseEntity) artifact;
			return addEntity(entity);
		} else if (artifact instanceof LighthouseRelationship) {
			LighthouseRelationship rel = (LighthouseRelationship) artifact;
			return addRelationship(rel);
		} else {
			return null;
		}
	}
	
	public LighthouseFile populateLHFile(Collection<LighthouseEntity> listEntities, Collection<LighthouseRelationship> listRel) {
		for (LighthouseEntity entity : listEntities) {
			addEntity(entity);
		}
		for (LighthouseRelationship rel : listRel) {
			addRelationship(rel);
		}
		return lighthouseFile;
	}
	
}
