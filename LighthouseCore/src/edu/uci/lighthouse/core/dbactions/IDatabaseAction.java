package edu.uci.lighthouse.core.dbactions;

import edu.uci.lighthouse.model.jpa.JPAException;

public interface IDatabaseAction {

	public void run() throws JPAException;
	
}
