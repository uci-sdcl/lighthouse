package edu.uci.lighthouse.model;

import javax.persistence.Entity;

/**
 * Represents a Method for the Lighthouse Model.
 * 
 * @author tproenca
 * 
 */
@Entity
public class LighthouseMethod extends LighthouseEntity {

	protected LighthouseMethod() {
		this("");
	}

	public LighthouseMethod(String fqn) {
		super(fqn);
	}

}
