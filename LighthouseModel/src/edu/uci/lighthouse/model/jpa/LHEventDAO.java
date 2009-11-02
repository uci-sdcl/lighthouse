package edu.uci.lighthouse.model.jpa;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.uci.lighthouse.model.LighthouseEvent;

public class LHEventDAO extends AbstractDAO<LighthouseEvent, Integer> {
	
	@Override
	public LighthouseEvent save(LighthouseEvent entity) throws JPAUtilityException {
		Date timestamp = getCurrentTimestamp();
		entity.setTimestamp(timestamp);
		return super.save(entity);
	}

	public List<LighthouseEvent> executeQueryEntitiesAndTime(
			Map<String, Date> parameters) {
		String strQuery = "SELECT event " + "FROM LighthouseEvent event "
				+ "WHERE ";
		strQuery += " ( ";
		for (Map.Entry<String, Date> entry : parameters.entrySet()) {
			String fqn = entry.getKey();
			Date timestamp = entry.getValue();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String strTimestamp = formatter.format(timestamp);
			strQuery += " ( ";
			strQuery += "event.entity" + " = " + "'" + fqn + "'";
			strQuery += " AND ";
			strQuery += "event.timestamp" + " >= " + "'" + strTimestamp + "'";
			strQuery += " ) ";
			strQuery += "OR ";
		}
		strQuery = strQuery.substring(0, strQuery.lastIndexOf("OR"));
		strQuery += " )";
		return executeDynamicQuery(strQuery);
	}
	
	public void updateCommittedEvents(List<String> listClazz, String authorName) {
		String command = 	"UPDATE LighthouseEvent e " +
							"SET e.committed = 1 " +
							"WHERE e.author.name = " + "'" + authorName + "'" + " " +
							"AND e.entity.fullyQualifiedName IN ( " +
							"SELECT rel.primaryKey.from " +
							"FROM LighthouseRelationship rel " +
							"WHERE rel.primaryKey.type = 0 ";
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
