package edu.uci.lighthouse.core.dbactions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import edu.uci.lighthouse.model.jpa.JPAException;

public class CompoundAction implements IDatabaseAction {

	private static final long serialVersionUID = -7628446691313339816L;
	List<IDatabaseAction> actions = new LinkedList<IDatabaseAction>();
	
	@Override
	public void run() throws JPAException {
		for (IDatabaseAction action : actions){
			action.run();
		}
	}
	
	public void add(IDatabaseAction action) {
		actions.add(action);
	}
	
	public Collection<IDatabaseAction> getActions() {
		return actions;
	}
}
