package edu.uci.lighthouse.model;

import javax.persistence.Entity;

@Entity
public class LighthouseField extends LighthouseEntity {

	protected LighthouseField() {
		this("");
	}

	public LighthouseField(String fqn) {
		super(fqn);
	}

}
