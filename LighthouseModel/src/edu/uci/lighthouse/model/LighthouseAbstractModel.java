package edu.uci.lighthouse.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import edu.uci.lighthouse.model.LighthouseRelationship.TYPE;

/**
 * It consist of a list of entities and a list of relationships
 * */
public abstract class LighthouseAbstractModel {

	private HashMap<String, LighthouseEntity> entities = new HashMap<String, LighthouseEntity>();

	private HashMap<String, LinkedHashSet<LighthouseRelationship>> relationships = new HashMap<String, LinkedHashSet<LighthouseRelationship>>();

	/** 
	 * Only {@link LighthouseModelManager} is allowed to call this method
	 * */
	final synchronized void addEntity(LighthouseEntity entity) {
		entities.put(entity.getFullyQualifiedName(), entity);
	}

	/** 
	 * Only {@link LighthouseModelManager} is allowed to call this method
	 * */
	final synchronized void removeEntity(LighthouseEntity entity) {
		entities.remove(entity.getFullyQualifiedName());
	}
	
	/** 
	 * Only {@link LighthouseModelManager} is allowed to call this method
	 * */
	final synchronized void addRelationship(LighthouseRelationship rel) {
		LighthouseEntity e = rel.getToEntity();		
		LinkedHashSet<LighthouseRelationship> list = relationships.get(e
				.getFullyQualifiedName());		
		if (list == null){
			list = new LinkedHashSet<LighthouseRelationship>();
			relationships.put(e.getFullyQualifiedName(), list);
		}
		list.add(rel);
	}
	
	/** 
	 * Only {@link LighthouseModelManager} is allowed to call this method
	 * */
	final synchronized void removeRelationship(LighthouseRelationship rel) {
		LighthouseEntity entity = rel.getToEntity();
		Collection<LighthouseRelationship> list = relationships.get(entity.getFullyQualifiedName());
		if (list!=null) { 
			list.remove(rel);
		}
	}

	/**
	 * Verify if a entity exist in the model
	 * */
	public boolean containsEntity(String fqn) {
		return (this.getEntity(fqn)!=null);
	}
	
	public LighthouseEntity getEntity(String fqn) {
		return entities.get(fqn);
	}
	
	public Collection<LighthouseEntity> getMethodsAndAttributesFromClass(
			LighthouseClass c) {
		LinkedList<LighthouseEntity> result = new LinkedList<LighthouseEntity>();
		Collection<LighthouseRelationship> list = relationships.get(c
				.getFullyQualifiedName());
		if (list != null){
			for (LighthouseRelationship r : list) {
				if (((r.getFromEntity() instanceof LighthouseField) || r
						.getFromEntity() instanceof LighthouseMethod)
						&& (r.getType() == LighthouseRelationship.TYPE.INSIDE)) {
					result.add(r.getFromEntity());
				}
			}
		}
		return result;
	}
	
	public Collection<LighthouseClass> getAllClasses() {
		LinkedList<LighthouseClass> list = new LinkedList<LighthouseClass>();
		for (LighthouseEntity e : entities.values()) {
			if (e instanceof LighthouseClass) {
				list.add((LighthouseClass) e);
			}
		}
		return list;
	}
	
	public Collection<LighthouseEntity> getEntities() {
		return entities.values();
	}
	
	public boolean containsRelationship(LighthouseRelationship relationship) {		
		LinkedHashSet<LighthouseRelationship> listOfAllRelationships = getRelationships();
		return listOfAllRelationships.contains(relationship);
	}
	
	/**
	 * Verify if the <code>relationship</code> exist in the model
	 * @return the relationship instance from the model 
	 * */
	public LighthouseRelationship getRelationship(LighthouseRelationship relationship) {
		Collection<LighthouseRelationship> list = getRelationshipsFrom(relationship.getFromEntity());
		for (LighthouseRelationship lighthouseRelationship : list) {
			if (lighthouseRelationship.equals(relationship)) {
				return lighthouseRelationship;
			}
		}
		return null;
	}
	
	public LinkedHashSet<LighthouseRelationship> getRelationships() {
		LinkedHashSet<LighthouseRelationship> result = new LinkedHashSet<LighthouseRelationship>();
		for (LinkedHashSet<LighthouseRelationship> list : relationships.values()) {			
			result.addAll(list);			
		}
		return result;
	}
	
	public Collection<LighthouseRelationship> getRelationshipsFrom(LighthouseEntity entity) {
		List<LighthouseRelationship> result = new LinkedList<LighthouseRelationship>();
		// FIXME Use this code for optimization - relationships.get(entity.getFullyQualifiedName()).iterator();
		for (Collection<LighthouseRelationship> list : relationships.values()) {			
			for (LighthouseRelationship rel : list) {			
				if (rel.getFromEntity().getFullyQualifiedName().compareTo(entity.getFullyQualifiedName()) == 0)
					result.add(rel);			
			}
		}
		return result;
	}
	
	public Collection<LighthouseRelationship> getRelationshipsTo(LighthouseEntity entity) {
		List<LighthouseRelationship> result = new LinkedList<LighthouseRelationship>();
		// FIXME Use this code for optimization - relationships.get(entity.getFullyQualifiedName()).iterator();
		for (Collection<LighthouseRelationship> list : relationships.values()) {
			for (LighthouseRelationship rel : list) {			
				if (rel.getToEntity().getFullyQualifiedName().compareTo(entity.getFullyQualifiedName()) == 0)
					result.add(rel);			
			}
		}
		return result;
	}

	public Collection<LighthouseRelationship> getRelationshipsTo(LighthouseEntity entity, TYPE type) {
		List<LighthouseRelationship> result = new LinkedList<LighthouseRelationship>();
		// FIXME Use this code for optimization - relationships.get(entity.getFullyQualifiedName()).iterator();
		for (Collection<LighthouseRelationship> list : relationships.values()) {
			for (LighthouseRelationship rel : list) {			
				if (rel.getToEntity().getFullyQualifiedName().compareTo(entity.getFullyQualifiedName()) == 0)
					if (rel.getType()==type) {
						result.add(rel);
					}
			}
		}
		return result;
	}
	
}
