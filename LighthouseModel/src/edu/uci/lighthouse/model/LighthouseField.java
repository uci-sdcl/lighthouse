package edu.uci.lighthouse.model;

import javax.persistence.Entity;

/**
 * Represents a Field for the Lighthouse Model.
 * 
 * @author tproenca
 * 
 */
@Entity
public class LighthouseField extends LighthouseEntity {

	protected LighthouseField() {
		super("");
	}

	public LighthouseField(String fqn) {
		super(fqn);
	}

}
