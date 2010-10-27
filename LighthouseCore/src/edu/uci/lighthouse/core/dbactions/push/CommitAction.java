package edu.uci.lighthouse.core.dbactions.push;

import java.sql.SQLException;
import java.util.Collection;

import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.jpa.JPAException;

public class CommitAction extends FileEventAction {
	
	private static final long serialVersionUID = 4506601483048744221L;
	
	private boolean timeAdjusted = false;

	public CommitAction(Collection<LighthouseEvent> events) {
		super(events);
	}

	@Override
	public void run() throws JPAException {
		try {
			if (!timeAdjusted) {
				ModelUtility.adjustCommittedTimeToServerTime(getEvents());
				timeAdjusted = true;
			}
			super.run();
		} catch (SQLException e) {
			throw new JPAException(e);
		}
	}
}
