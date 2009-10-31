package edu.uci.lighthouse.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;


/**
 * @author nilmax
 */
public class CheckedOutEntities implements Serializable {

	private static final long serialVersionUID = -2471477769693559865L;

	private HashMap<String, Date> mapEntityCheckInTime = new HashMap<String, Date>();

	/**
	 * 
	 * @param fqn
	 *            Entity fully qualified name
	 * @return Check in time of the input <code>fqn</code> entity
	 * */
	public Date get(String fqn) {
		return mapEntityCheckInTime.get(fqn);
	}

	public void put(String fqn, Date checkInTime) {
		mapEntityCheckInTime.put(fqn, checkInTime);
	}
	
	public Set<Entry<String, Date>> getEntrySet() {
		return mapEntityCheckInTime.entrySet();
	}

}
