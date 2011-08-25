package edu.uci.lighthouse.model;

import javax.persistence.Entity;

import edu.uci.lighthouse.model.LighthouseEvent.TYPE;

/**
 * 
 * @author lee
 *
 */
@Entity
public abstract class PluginLighthouseEvent extends LighthouseEvent{
	
	public PluginLighthouseEvent(TYPE modify, LighthouseAuthor author,
			LighthouseEntity entity) {
		super(modify,author,entity);
	}

}
