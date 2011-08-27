package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.Observer;

/**
 * this interface is used to get around the problem of extending Observable when a Class
 * already extends another Class.  The idea is to have the implementer have an ObservablePoint A field and 
 * to call A.changed() when the implementer changes. 
 * @author lee
 *
 */
public interface IHasObservablePoint {

	public void observeMe(Observer observer);
	
	public ObservablePoint getObservablePoint();
	
}
