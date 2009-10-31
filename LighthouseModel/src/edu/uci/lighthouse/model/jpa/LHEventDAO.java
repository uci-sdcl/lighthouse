package edu.uci.lighthouse.model.jpa;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.uci.lighthouse.model.LighthouseEvent;

public class LHEventDAO extends AbstractDAO<LighthouseEvent, Integer> {

	public List<LighthouseEvent> executeQueryEntitiesAndTime(
			Map<String, Date> parameters) {
		String strQuery = "SELECT event " + "FROM LighthouseEvent event "
				+ "WHERE ";
		strQuery += " ( ";
		for (Map.Entry<String, Date> entry : parameters.entrySet()) {
			String fqn = entry.getKey();
			Date timestamp = entry.getValue();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String strTimestamp = formatter.format(timestamp);
			strQuery += " ( ";
			strQuery += "event.entity" + " = " + "'" + fqn + "'";
			strQuery += " AND ";
			strQuery += "event.timestamp" + " >= " + "'" + strTimestamp + "'" ;
			strQuery += " ) ";
			strQuery += "OR ";
		}
		strQuery = strQuery.substring(0, strQuery.lastIndexOf("OR"));
		strQuery += " )";
		return executeDynamicQuery(strQuery);
	}
	
}
