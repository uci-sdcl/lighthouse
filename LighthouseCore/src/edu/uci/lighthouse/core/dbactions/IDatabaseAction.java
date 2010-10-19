package edu.uci.lighthouse.core.dbactions;

import java.io.Serializable;

import edu.uci.lighthouse.model.jpa.JPAException;

public interface IDatabaseAction extends Serializable {

	public void run() throws JPAException;
	
}
