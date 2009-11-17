package edu.uci.lighthouse.model;

import edu.uci.lighthouse.model.LighthouseRelationship.TYPE;


public class LighthouseFileManager {
	
	private LighthouseFile lighthouseFile;
	private LighthouseModel lighthouseModel = LighthouseModel.getInstance();

	public LighthouseFileManager(LighthouseFile lighthouseFileModel) {
		this.lighthouseFile = lighthouseFileModel; 
	}

	public LighthouseEntity addEntity(LighthouseEntity newEntity){
		LighthouseEntity entity = lighthouseModel.getEntity(newEntity.getFullyQualifiedName());
		if (entity==null) {
			entity = newEntity;
		}
		lighthouseFile.addEntity(entity);
		return entity;
	}
	
	public LighthouseRelationship addRelationship(LighthouseRelationship newRelationship){
		LighthouseRelationship relationship = lighthouseModel.getRelationship(newRelationship);
		if (relationship == null) {
			relationship = newRelationship;
		}
		handleEntitiesNotInsideClass(relationship);		
		lighthouseFile.addRelationship(relationship);
		return newRelationship;
	}

	/** Need this method for add ExternalClass and Modifiers in the LHBaseFile
	 * because they have not TYPE.INSIDE relationship */
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
	
}
