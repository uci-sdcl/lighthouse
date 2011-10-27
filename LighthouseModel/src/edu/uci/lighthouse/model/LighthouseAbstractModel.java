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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import edu.uci.lighthouse.model.LighthouseRelationship.TYPE;

/**
 * It consist of a list of entities and a list of relationships
 * */
@SuppressWarnings("serial")
public abstract class LighthouseAbstractModel implements Serializable {

	private HashMap<String, LighthouseEntity> entities = new HashMap<String, LighthouseEntity>();

	/** Indexed by ToEntity. */
	private HashMap<String, LinkedHashSet<LighthouseRelationship>> relationshipsTo = new HashMap<String, LinkedHashSet<LighthouseRelationship>>();

	/** Indexed by FromEntity. */
	private HashMap<String, LinkedHashSet<LighthouseRelationship>> relationshipsFrom = new HashMap<String, LinkedHashSet<LighthouseRelationship>>();
	
	private LinkedHashSet<String> packageNames = new LinkedHashSet<String>();
	
	private LinkedHashSet<String> projectNames = new LinkedHashSet<String>();
	
	private LinkedHashSet<LighthouseClass> classes = new LinkedHashSet<LighthouseClass>();
	
	private LinkedHashSet<LighthouseInterface> interfaces = new LinkedHashSet<LighthouseInterface>();

	/**
	 * Only {@link LighthouseModelManager} is allowed to call this method
	 * */
	protected synchronized void addEntity(LighthouseEntity entity) {
		/* We are interested just in classes and interfaces that belongs our project. External classes are not included. */
		if (entity instanceof LighthouseClass || entity instanceof LighthouseInterface) {
			packageNames.add(entity.getPackageName());
			projectNames.add(entity.getProjectName());
			if (entity instanceof LighthouseClass){
				classes.add((LighthouseClass)entity);
			} else if (entity instanceof LighthouseInterface) {
				interfaces.add((LighthouseInterface)entity);
			}
		}
		entities.put(entity.getFullyQualifiedName(), entity);
	}

	/**
	 * Only {@link LighthouseModelManager} is allowed to call this method
	 * */
	protected synchronized void removeEntity(LighthouseEntity entity) {
		/* Is not necessary remove project and package names since the list is populated everytime Lighthouse runs.*/
		classes.remove(entity);
		interfaces.remove(entity);
		entities.remove(entity.getFullyQualifiedName());
	}
	
	public Collection<String> getPackageNames(){
		return packageNames;
	}
	
	public Collection<String> getProjectNames(){
		return projectNames;
	}
	
	/**
	 * Only {@link LighthouseModelManager} is allowed to call this method
	 * */
	protected synchronized void addRelationship(LighthouseRelationship rel) {
		LinkedHashSet<LighthouseRelationship> listFrom = relationshipsFrom
				.get(rel.getFromEntity().getFullyQualifiedName());
		LinkedHashSet<LighthouseRelationship> listTo = relationshipsTo.get(rel
				.getToEntity().getFullyQualifiedName());
		if (listFrom == null) {
			listFrom = new LinkedHashSet<LighthouseRelationship>();
			relationshipsFrom.put(rel.getFromEntity().getFullyQualifiedName(),
					listFrom);
		}
		listFrom.add(rel);
		if (listTo == null) {
			listTo = new LinkedHashSet<LighthouseRelationship>();
			relationshipsTo.put(rel.getToEntity().getFullyQualifiedName(),
					listTo);
		}
		listTo.add(rel);
	}

	/**
	 * Only {@link LighthouseModelManager} is allowed to call this method
	 * */
	protected synchronized void removeRelationship(LighthouseRelationship rel) {
		Collection<LighthouseRelationship> listTo = relationshipsTo.get(rel
				.getToEntity().getFullyQualifiedName());
		if (listTo != null) {
			listTo.remove(rel);
		}
		Collection<LighthouseRelationship> listFrom = relationshipsFrom.get(rel
				.getFromEntity().getFullyQualifiedName());
		if (listFrom != null) {
			listFrom.remove(rel);
		}
	}

	/**
	 * Verify if a entity exist in the model
	 * */
	public boolean containsEntity(String fqn) {
		return (this.getEntity(fqn) != null);
	}

	public LighthouseEntity getEntity(String fqn) {
		return entities.get(fqn);
	}

	public Collection<LighthouseEntity> getMethodsAndAttributesFromClass(
			LighthouseEntity c) {
		LinkedList<LighthouseEntity> result = new LinkedList<LighthouseEntity>();
		Collection<LighthouseRelationship> list = relationshipsTo.get(c
				.getFullyQualifiedName());
		if (list != null) {
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
//		LinkedList<LighthouseClass> list = new LinkedList<LighthouseClass>();
//		for (LighthouseEntity e : entities.values()) {
//			if (e instanceof LighthouseClass) {
//				list.add((LighthouseClass) e);
//			}
//		}
//		return list;
		return classes;
	}
	
	public Collection<LighthouseInterface> getAllInterfaces() {
		return interfaces;
	}

	public Collection<LighthouseEntity> getEntities() {
		return entities.values();
	}

	public boolean containsRelationship(LighthouseRelationship relationship) {
		Collection<LighthouseRelationship> listOfAllRelationships = getRelationships();
		return listOfAllRelationships.contains(relationship);
	}

	/**
	 * Verify if the <code>relationship</code> exist in the model
	 * 
	 * @return the relationship instance from the model
	 * */
	public LighthouseRelationship getRelationship(
			LighthouseRelationship relationship) {
		Collection<LighthouseRelationship> list = getRelationshipsFrom(relationship
				.getFromEntity());
		if (list != null) {
			for (LighthouseRelationship lighthouseRelationship : list) {
				if (lighthouseRelationship.equals(relationship)) {
					return lighthouseRelationship;
				}
			}
		}
		return null;
	}

	public Collection<LighthouseRelationship> getRelationships() {
		Collection<LighthouseRelationship> result = new LinkedList<LighthouseRelationship>();
		for (LinkedHashSet<LighthouseRelationship> list : relationshipsTo
				.values()) {
			result.addAll(list);
		}
		return result;
	}

	public Collection<LighthouseRelationship> getRelationshipsFrom(
			LighthouseEntity entity) {
		LinkedHashSet<LighthouseRelationship> list = relationshipsFrom
				.get(entity.getFullyQualifiedName());
		return list != null ? list : new LinkedList<LighthouseRelationship>();
	}

	public Collection<LighthouseRelationship> getRelationshipsTo(
			LighthouseEntity entity) {
		LinkedHashSet<LighthouseRelationship> list = relationshipsTo.get(entity
				.getFullyQualifiedName());
		return list != null ? list : new LinkedList<LighthouseRelationship>();
	}

	public Collection<LighthouseRelationship> getRelationshipsTo(
			LighthouseEntity entity, TYPE type) {
		List<LighthouseRelationship> result = new LinkedList<LighthouseRelationship>();
		for (LighthouseRelationship rel : getRelationshipsTo(entity)) {
			if (rel.getType() == type) {
				result.add(rel);
			}
		}
		return result;
	}

	protected synchronized void clear(){
		entities.clear();
		relationshipsTo.clear();
		relationshipsFrom.clear();
		packageNames.clear();
		projectNames.clear();
		classes.clear();
		interfaces.clear();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classes == null) ? 0 : classes.hashCode());
		result = prime * result
				+ ((entities == null) ? 0 : entities.hashCode());
		result = prime * result
				+ ((interfaces == null) ? 0 : interfaces.hashCode());
		result = prime * result
				+ ((packageNames == null) ? 0 : packageNames.hashCode());
		result = prime * result
				+ ((projectNames == null) ? 0 : projectNames.hashCode());
		result = prime
				* result
				+ ((relationshipsFrom == null) ? 0 : relationshipsFrom
						.hashCode());
		result = prime * result
				+ ((relationshipsTo == null) ? 0 : relationshipsTo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LighthouseAbstractModel other = (LighthouseAbstractModel) obj;
		if (classes == null) {
			if (other.classes != null)
				return false;
		} else if (!classes.equals(other.classes))
			return false;
		if (entities == null) {
			if (other.entities != null)
				return false;
		} else if (!entities.equals(other.entities))
			return false;
		if (interfaces == null) {
			if (other.interfaces != null)
				return false;
		} else if (!interfaces.equals(other.interfaces))
			return false;
		if (packageNames == null) {
			if (other.packageNames != null)
				return false;
		} else if (!packageNames.equals(other.packageNames))
			return false;
		if (projectNames == null) {
			if (other.projectNames != null)
				return false;
		} else if (!projectNames.equals(other.projectNames))
			return false;
		if (relationshipsFrom == null) {
			if (other.relationshipsFrom != null)
				return false;
		} else if (!relationshipsFrom.equals(other.relationshipsFrom))
			return false;
		if (relationshipsTo == null) {
			if (other.relationshipsTo != null)
				return false;
		} else if (!relationshipsTo.equals(other.relationshipsTo))
			return false;
		return true;
	}
	
	protected synchronized void assignTo(LighthouseAbstractModel model){
		entities = model.entities;
		relationshipsTo = model.relationshipsTo;
		relationshipsFrom = model.relationshipsFrom;
		packageNames = model.packageNames;
		projectNames = model.projectNames;
		classes = model.classes;
		interfaces = model.interfaces;
	}
	
}
