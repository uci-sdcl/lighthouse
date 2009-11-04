package edu.uci.lighthouse.core.parser;

import java.util.ArrayList;
import java.util.Set;

import edu.uci.ics.sourcerer.model.Modifier;
import edu.uci.lighthouse.model.LighthouseAbstractModel;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseExternalClass;
import edu.uci.lighthouse.model.LighthouseField;
import edu.uci.lighthouse.model.LighthouseInterface;
import edu.uci.lighthouse.model.LighthouseMethod;
import edu.uci.lighthouse.model.LighthouseModifier;
import edu.uci.lighthouse.model.LighthouseRelationship;

public class BuilderEntity {

	private static BuilderEntity instance = null;	
	private static ArrayList<EntityNode> listEntity = new ArrayList<EntityNode>();	
	public static enum EntityType {
		CLASS, INTERFACE, FIELD, METHOD, EXTERNAL_CLASS
	}

	private BuilderEntity() {

	}

	public static BuilderEntity getInstance() {
		if (instance == null) {
			instance = new BuilderEntity();
		}
		return instance;
	}

	public void populateAllEntityToModel(LighthouseAbstractModel model) {
		for (EntityNode entityNode : listEntity) {
			if (!LighthouseParser.isEntityUnresolved(entityNode.fqn)) {
				String fqn = entityNode.fqn;			
				int modifiers = entityNode.modifiers;
				EntityType type = entityNode.type;	
				LighthouseEntity entity = null;
				switch (type) {
				case CLASS:
					entity = new LighthouseClass(fqn);
					break;
				case INTERFACE:
					entity = new LighthouseInterface(fqn);
					break;
				case FIELD:
					entity = new LighthouseField(fqn);
					break;
				case METHOD:
					entity = new LighthouseMethod(fqn);
					break;
				case EXTERNAL_CLASS:
					entity = new LighthouseExternalClass(fqn);
					break;
				} // end-of-switch
				if (entity!=null) {
					ParserEventManager.addEntity(model, entity);
					addModifiers(model, entity, modifiers);
				}
			} // end-of-entityUnresolved
		} // end-of-for
		
		/*
		 * after populate the model, clean the list of entities because this is a
		 * singleton instance
		 */	
		removeAll();
	}
	
	private void addModifiers(LighthouseAbstractModel model, LighthouseEntity entity, int modifiers) {
		Set<Modifier> setModifiers = Modifier.convertFromInt(modifiers);
		for (Modifier utilModifier : setModifiers) {
			LighthouseModifier modifier = new LighthouseModifier(utilModifier.toString());
			LighthouseRelationship relationship = new LighthouseRelationship(entity,modifier,LighthouseRelationship.TYPE.MODIFIED_BY);
			ParserEventManager.addEntity(model, modifier);
			ParserEventManager.addRelationship(model, relationship);
		}
	}

	public void addEntity(String fqn, int modifiers, EntityType type) {
		listEntity.add(new EntityNode(fqn, modifiers, type));
	}
	
	private void removeAll() {
		if (listEntity!=null) {
			listEntity.clear();
		}		
	}

	private class EntityNode {

		String fqn;
		int modifiers;
		EntityType type;

		public EntityNode(String fqn, int modifiers, EntityType type) {
			this.fqn = fqn;
			this.modifiers = modifiers;
			this.type = type;
		}
	}	
	
}
