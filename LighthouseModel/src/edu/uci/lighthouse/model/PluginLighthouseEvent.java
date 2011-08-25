package edu.uci.lighthouse.model;

import java.io.Serializable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import edu.uci.lighthouse.model.LighthouseEvent.TYPE;

/**
 * 
 * @author lee
 *
 */
@Entity
@DiscriminatorValue("p")
public abstract class PluginLighthouseEvent extends LighthouseEvent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5199155492765275134L;

	public PluginLighthouseEvent(TYPE modify, LighthouseAuthor author,
			LighthouseEntity entity) {
		super(modify,author,entity);
	}

}
