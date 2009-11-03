package edu.uci.lighthouse.core.controller;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;


/**
 * @author nilmax
 */
public class WorkingCopyClasses implements Serializable {

	private static final long serialVersionUID = -2471477769693559865L;

	private HashMap<String, Date> mapEntityCheckInTime = new HashMap<String, Date>();

	/**
	 * lastRevisionTimestamp = commit Time
	 * 
	 * @param classFqn
	 * @param lastRevisionTimestamp
	 */
	public void addClassFullyQualifiedName(String classFqn, Date lastRevisionTimestamp) {
		mapEntityCheckInTime.put(classFqn, lastRevisionTimestamp);
	}
	
	public void removeClassFullyQualifiedName(String classFqn){
		mapEntityCheckInTime.remove(classFqn);
	}
	
	public Collection<String> getAllClassesFullyQualifiedName(){
		return mapEntityCheckInTime.keySet();
	}
	
	public Date getLastRevisionTimestamp(String classFqn) {
		return mapEntityCheckInTime.get(classFqn);
	}
}
