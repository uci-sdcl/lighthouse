package edu.uci.lighthouse.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

@SuppressWarnings("serial")
public class CheckedOutEntities implements Serializable {

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

}
