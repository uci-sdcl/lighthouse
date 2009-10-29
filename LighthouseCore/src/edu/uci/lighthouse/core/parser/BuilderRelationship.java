package edu.uci.lighthouse.core.parser;

import java.util.ArrayList;

import edu.uci.lighthouse.model.LighthouseAbstractModel;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseExternalClass;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.LighthouseRelationship.TYPE;
import edu.uci.lighthouse.model.jpa.JPAUtilityException;

public class BuilderRelationship {

	private static BuilderRelationship instance = null;
	private static ArrayList<RelationshipNode> listRelationships = new ArrayList<RelationshipNode>();
	private static LighthouseModel lighthouseModel = LighthouseModel.getInstance();

	private BuilderRelationship() {

	}

	public static BuilderRelationship getInstance() {
		if (instance == null) {
			instance = new BuilderRelationship();
		}
		return instance;
	}

	public void populateAllRelationshipsToModel(LighthouseAbstractModel model) throws JPAUtilityException {		
		for (RelationshipNode relationshipNode : listRelationships) {
			String fqnFrom = relationshipNode.from;
			String fqnTo = relationshipNode.to;
			TYPE type = relationshipNode.type;

			if (!LighthouseParser.isEntityUnresolved(fqnFrom) && !LighthouseParser.isEntityUnresolved(fqnTo)) {
				LighthouseEntity entityFrom = lighthouseModel.getEntity(fqnFrom);
				LighthouseEntity entityTo = lighthouseModel.getEntity(fqnTo);
				
				// If entityFrom is null we need to check the entity inside the LighthouseFile(model)
				if (entityFrom==null) {
					entityFrom = model.getEntity(fqnFrom);
				}
				
				// If entityTo is null we need to check the entity inside the LighthouseFile(model)
				if (entityTo==null) { 
					entityTo = model.getEntity(fqnTo);
					// If entityTo still null, then I need to check the database
					if (entityTo==null) {
						entityTo = ParserEventManager.getEntityFromDataBase(fqnTo);
						// If entityTo still null, then it is an external class
						if (entityTo == null) {
							entityTo = new LighthouseExternalClass(fqnTo);
							// The following line should to stay inside the if
							// statement, because we can not add an entity from
							// the Database into a LHFile if the "entityTo" does
							// not belong to the class(file) being parsed
							entityTo = ParserEventManager.addEntity(model, entityTo);
						}
					}				
				}
				
				if (entityFrom != null && entityTo != null) {
					LighthouseRelationship relationship = new LighthouseRelationship(entityFrom,entityTo,type);
					ParserEventManager.addRelationship(model, relationship);
				}				
			} // end-of-if-unresolved

		} // end-of-for
		/*
		 * after populate the model, clean the list of entities because this is a
		 * singleton instance
		 */
		removeAll();
	}
	
	public void addRelantionship(String fqnFrom, String fqnTo, TYPE type) {
		listRelationships.add(new RelationshipNode(fqnFrom, fqnTo, type));
	}

	private void removeAll() {
		if (listRelationships!=null) {
			listRelationships.clear();
		}
	}	
	
	private class RelationshipNode {

		String from;
		String to;
		TYPE type;

		public RelationshipNode(String from, String to, TYPE type) {
			this.from = from;
			this.to = to;
			this.type = type;
		}
	}
}
