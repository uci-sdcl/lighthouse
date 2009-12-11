package edu.uci.lighthouse.core.controller;

import java.util.Date;
import java.util.List;

import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.jpa.JPAUtilityException;
import edu.uci.lighthouse.model.jpa.LHRepositoryEventDAO;
import edu.uci.lighthouse.model.repository.LighthouseRepositoryEvent;
import edu.uci.lighthouse.model.repository.LighthouseRepositoryEvent.TYPE;

public class PushRepositoryEvents {

	public void saveEvent(List<String> classNames, LighthouseAuthor author,
			TYPE type, Date eventTime, List<Number> versionsAffected)
			throws JPAUtilityException {

		// If there is not a version number for each class name, return
		if (classNames.size() != versionsAffected.size())
			return;

		for (int i = 0; i < classNames.size(); i++) {

			LighthouseRepositoryEvent e = new LighthouseRepositoryEvent();
			e.setAuthor(author);
			e.setType(type);
			e.setClassName(classNames.get(i));
			e.setEventTime(eventTime);
			e.setVersionAffected(versionsAffected.get(i).longValue());

			LHRepositoryEventDAO dao = new LHRepositoryEventDAO();
			dao.save(e);
		}

	}

}
