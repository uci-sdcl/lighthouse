package edu.uci.lighthouse.core.data;

import java.util.List;
import java.util.Observer;

import edu.uci.lighthouse.model.LighthouseEvent;

/**
 * Classes that want to receive LighthouseEvents when the are pulled from 
 * the Database Model should implement this interface. 
 * @author lee
 *
 */
public interface ISubscriber{

	public void receive(List<LighthouseEvent> events);
}
