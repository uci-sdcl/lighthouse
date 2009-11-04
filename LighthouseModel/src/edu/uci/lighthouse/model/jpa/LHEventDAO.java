package edu.uci.lighthouse.model.jpa;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.uci.lighthouse.model.LighthouseEvent;

public class LHEventDAO extends AbstractDAO<LighthouseEvent, Integer> {
	
	public List<LighthouseEvent> executeQueryEntitiesAndTime(
			Map<String, Date> parameters) {
		String strQuery = "SELECT e " + "FROM LighthouseEvent e "
				+ "WHERE ";
		strQuery += " ( ";
		
		// Get all entities' events
		for (Map.Entry<String, Date> entry : parameters.entrySet()) {
			String fqn = entry.getKey();
			Date timestamp = entry.getValue();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String strTimestamp = formatter.format(timestamp);
			strQuery += " ( ( ";
			strQuery += "e.entity" + " = " + "'" + fqn + "'";
			strQuery += " AND ";
			strQuery += "e.timestamp" + " >= " + "'" + strTimestamp + "'";
			strQuery += " ) OR ( ( ";
			strQuery += "e.entity" + " = " + "'" + fqn + "'";
			strQuery += " AND ";
			strQuery += "e.isCommitted" + " = " + "1";
			strQuery += " ) AND ";
			strQuery += " NOT ( ";
			strQuery += "e.type" + " = " + "1"; // type==remove
			strQuery += " AND ";
			strQuery += "e.committedTime" + " <= " + "'" + strTimestamp + "'";
			strQuery += " ) ) ) ";
			strQuery += "OR ";
		}
		
		// Get all relationships' events
		for (Map.Entry<String, Date> entry : parameters.entrySet()) {
			String fqn = entry.getKey();
			Date timestamp = entry.getValue();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String strTimestamp = formatter.format(timestamp);
			strQuery += " ( ( ";
			strQuery += " ( ";
			strQuery += "e.relationship.primaryKey.from" + " = " + "'" + fqn + "'";
			strQuery += " OR ";
			strQuery += "e.relationship.primaryKey.to" + " = " + "'" + fqn + "'";
			strQuery += " ) ";
			strQuery += " AND ";
			strQuery += "e.timestamp" + " >= " + "'" + strTimestamp + "'";
			strQuery += " ) OR ( ( ";
			strQuery += " ( ";
			strQuery += "e.relationship.primaryKey.from" + " = " + "'" + fqn + "'";
			strQuery += " OR ";
			strQuery += "e.relationship.primaryKey.to" + " = " + "'" + fqn + "'";
			strQuery += " ) ";
			strQuery += " AND ";
			strQuery += "e.isCommitted" + " = " + "1";
			strQuery += " ) AND ";
			strQuery += " NOT ( ";
			strQuery += "e.type" + " = " + "1"; // type==remove
			strQuery += " AND ";
			strQuery += "e.committedTime" + " <= " + "'" + strTimestamp + "'";
			strQuery += " ) ) ) ";
			strQuery += "OR ";
		}
		
		strQuery = strQuery.substring(0, strQuery.lastIndexOf("OR"));
		strQuery += " )";
		return executeDynamicQuery(strQuery);
	}
	
	/** NOT in use anynmore*/
	public void updateCommittedEvents(List<String> listClazz, String authorName) throws JPAUtilityException {
		String command = 	"UPDATE LighthouseEvent e " +
							"SET e.isCommitted = 1 " +
							", e.committedTime = CURRENT_TIMESTAMP " +
							"WHERE e.author.name = " + "'" + authorName + "'" + " " +
							"AND e.isCommitted = 0 " +
							"AND e.entity.fullyQualifiedName IN ( " +
							"SELECT rel.primaryKey.from " +
							"FROM LighthouseRelationship rel " +
							"WHERE rel.primaryKey.type = 0 "; // type==inside
		command+= "AND ( ";
		for (String fqnClazz : listClazz) {
			command+= "rel.primaryKey.to = " + "'" + fqnClazz + "'";
			command+= " OR ";
		}
		command = command.substring(0, command.lastIndexOf("OR"));
		command += " ) )";
		executeUpdateQuery(command);
		
	}
}
