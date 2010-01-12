package edu.uci.lighthouse.model;

import javax.persistence.Entity;

@Entity
public class LighthouseMethod extends LighthouseEntity {

	protected LighthouseMethod() {
		this("");
	}

	public LighthouseMethod(String fqn) {
		super(fqn);
	}

}
