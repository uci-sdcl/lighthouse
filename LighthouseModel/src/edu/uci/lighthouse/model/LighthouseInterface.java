package edu.uci.lighthouse.model;

import javax.persistence.Entity;

@Entity
public class LighthouseInterface extends LighthouseEntity {

	protected LighthouseInterface() {
	}

	public LighthouseInterface(String fqn) {
		super(fqn);
	}

}
