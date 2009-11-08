package edu.uci.lighthouse.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.uci.lighthouse.model.jpa.JPAUtilityException;
import edu.uci.lighthouse.model.jpa.LHEntityDAO;
import edu.uci.lighthouse.model.jpa.LHEventDAO;
import edu.uci.lighthouse.model.jpa.LHRelationshipDAO;
import edu.uci.lighthouse.model.util.UtilModifiers;

/**
 * This is a utility class for building model objects. Its purpose is to
 * assist in constructing models for test cases, but it is useful any time a
 * model needs to be constructed from code.
 * 
 * All the methods are static.
 * 
 * @author tproenca
 * 
 */
public class LighthouseModelManager {
	
	private static Logger logger = Logger.getLogger(LighthouseModelManager.class);
	
	protected LighthouseModel model;

	public LighthouseModelManager(LighthouseModel model) {
		super();
		this.model = model;
	}

	private LighthouseEntity addEntity(LighthouseEntity newEntity){
		LighthouseEntity entity = model.getEntity(newEntity.getFullyQualifiedName());
		if (entity!=null) {
			return entity;
		} else {
			model.addEntity(newEntity);
			return newEntity;	
		}
	}
	
	private LighthouseRelationship addRelationship(LighthouseRelationship newRelationship){
		LighthouseRelationship relationship = model.getRelationship(newRelationship);
		if (relationship!=null) {
			return relationship; 
		} else { // I need to keep the old instance of entities because of JPA integrity
			LighthouseEntity entityFrom = model.getEntity(newRelationship.getFromEntity().getFullyQualifiedName());
			LighthouseEntity entityTo = model.getEntity(newRelationship.getToEntity().getFullyQualifiedName());
			if (entityFrom!=null && entityTo!=null) {
				newRelationship.setFromEntity(entityFrom);
				newRelationship.setToEntity(entityTo);
				model.addRelationship(newRelationship);
			} else {
				logger.error("Trying to add an invalid relationship: " + relationship);
			}
			return newRelationship;
		}
	}
	
	/** Add <code>event</code> in the LighthouseModel, however do not add the event in the database*/
	public void addEvent(LighthouseEvent event) {
		Object artifact = event.getArtifact();
		if (artifact instanceof LighthouseEntity) {
			LighthouseEntity entity = addEntity((LighthouseEntity) artifact);
			event.setArtifact(entity);
		} else if (artifact instanceof LighthouseRelationship) {
			LighthouseRelationship relationship = addRelationship((LighthouseRelationship) artifact);
			event.setArtifact(relationship);
		} else {
			logger.warn("Event Artifact is null: " + event.toString());
		}
		model.addEvent(event);
	}
	
	/** Add <code>event</code> in the LighthouseModel, however do not add the event in the database */
	public void removeEvent(LighthouseEvent event) {
		model.removeEvent(event);
	}
	
	public LighthouseEntity getEntity(String fqn) {
		return model.getEntity(fqn);
	}
	
	
	// NOVO
	
	public void saveEventsIntoDatabase(Collection<LighthouseEvent> listEvents) throws JPAUtilityException {
		LHEventDAO dao = new LHEventDAO();
		Date currentDBTimestamp = dao.getCurrentTimestamp();
		for (LighthouseEvent event : listEvents) {
			event.setTimestamp(currentDBTimestamp);
			dao.save(event);
		}
	}
	
	public LighthouseEntity getEntityFromDatabase(String fqn) {
		LighthouseEntity entity = getEntity(fqn);
		if (entity==null) {
			entity = new LHEntityDAO().get(fqn);
		}
		return entity;
	}
	
	public LinkedHashSet<LighthouseEntity> getEntitiesInsideClass(List<String> listClazzFqn) {
		LinkedHashSet<LighthouseEntity> listFromEntities = new LinkedHashSet<LighthouseEntity>();
		for (String clazzFqn : listClazzFqn) {
			listFromEntities.addAll(getEntitiesInsideClass(clazzFqn));			
		}
		return listFromEntities;
	}
	
	public  List<LighthouseEntity> getEntitiesInsideClass(String clazzFqn) {
//		LighthouseEntity clazz = getEntity(clazzFqn);
		
		LighthouseClass entity = new LighthouseClass(clazzFqn);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("relType", LighthouseRelationship.TYPE.INSIDE);
		parameters.put("toEntity", entity);
		return new LHRelationshipDAO().executeNamedQueryGetFromEntityFqn("LighthouseRelationship.findFromEntityByTypeAndToEntity", parameters);
	}
	
	//
//	public static LighthouseField addFieldInsideClass(LighthouseAbstractModel model, LighthouseClass aClass, String fieldShortName){
//		String fieldFqn = aClass.getFullQualifiedName() + "." + fieldShortName;
//		LighthouseField field = addField(model, fieldFqn);
//		addRelationship(model,field,aClass,LighthouseRelationship.TYPE.INSIDE);
//		return field;
//	}
//	
//	public static LighthouseMethod addMethodInsideClass(LighthouseAbstractModel model, LighthouseClass aClass, String methodShortName){
//		String methodFqn = aClass.getFullQualifiedName()+"."+methodShortName;
//		LighthouseMethod method = addMethod(model, methodFqn);
//		addRelationship(model, method, aClass, LighthouseRelationship.TYPE.INSIDE);
//		return method;	
//	}
//	
//	public static LighthouseRelationship addUsesRelationship(LighthouseAbstractModel model, LighthouseEntity from, LighthouseEntity to){
//		return addRelationship(model,from,to,LighthouseRelationship.TYPE.USES);
//	}
	
	/**
	 * Returns the associate class or <code>null</code> otherwise.
	 * 
	 * @param model
	 * @param e
	 * @return
	 */
	public LighthouseClass getMyClass(LighthouseEntity e){
		String classFqn = null;
		if (e instanceof LighthouseClass){
			classFqn = e.getFullyQualifiedName();
		} else if (e instanceof LighthouseField){
			classFqn = e.getFullyQualifiedName().replaceAll("(\\.\\w+)\\z", "");
		} else if (e instanceof LighthouseMethod){
			classFqn = e.getFullyQualifiedName().replaceAll("\\.[\\<\\w]\\w+[\\>\\w]\\([,\\w\\.]*\\)","");
		}
		LighthouseEntity c = model.getEntity(classFqn);
		if (c instanceof LighthouseClass){
			return (LighthouseClass)c;
		}
		return null;
	}
	
	public boolean isStatic(LighthouseEntity e){
		Collection<LighthouseRelationship> list =  model.getRelationshipsFrom(e);
		for (LighthouseRelationship r : list) {
			if (r.getToEntity() instanceof LighthouseModifier){
				if (UtilModifiers.isStatic(r.getToEntity().getFullyQualifiedName())){
					return true;
				}
			}
		}
		return false;
	}
	public boolean isFinal(LighthouseEntity e){
		Collection<LighthouseRelationship> list =  model.getRelationshipsFrom(e);
		for (LighthouseRelationship r : list) {
			if (r.getToEntity() instanceof LighthouseModifier){
				if (UtilModifiers.isFinal(r.getToEntity().getFullyQualifiedName())){
					return true;
				}
			}
		}
		return false;
	}
	public boolean isSynchronized(LighthouseEntity e){
		Collection<LighthouseRelationship> list =  model.getRelationshipsFrom(e);
		for (LighthouseRelationship r : list) {
			if (r.getToEntity() instanceof LighthouseModifier){
				if (UtilModifiers.isSynchronized(r.getToEntity().getFullyQualifiedName())){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isPublic(LighthouseEntity e){
		Collection<LighthouseRelationship> list =  model.getRelationshipsFrom(e);
		for (LighthouseRelationship r : list) {
			if (r.getToEntity() instanceof LighthouseModifier){
				if (UtilModifiers.isPublic(r.getToEntity().getFullyQualifiedName())){
					return true;
				}
			}
		}
		return false;
	}
	public boolean isProtected(LighthouseEntity e){
		Collection<LighthouseRelationship> list =  model.getRelationshipsFrom(e);
		for (LighthouseRelationship r : list) {
			if (r.getToEntity() instanceof LighthouseModifier){
				if (UtilModifiers.isProtected(r.getToEntity().getFullyQualifiedName())){
					return true;
				}
			}
		}
		return false;
	}
	public boolean isPrivate(LighthouseEntity e){
		Collection<LighthouseRelationship> list =  model.getRelationshipsFrom(e);
		for (LighthouseRelationship r : list) {
			if (r.getToEntity() instanceof LighthouseModifier){
				if (UtilModifiers.isPrivate(r.getToEntity().getFullyQualifiedName())){
					return true;
				}
			}
		}
		return false;
	}
	
}
