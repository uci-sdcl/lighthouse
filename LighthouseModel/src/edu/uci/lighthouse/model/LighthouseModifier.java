package edu.uci.lighthouse.model;

import javax.persistence.Entity;

@Entity
public class LighthouseModifier extends LighthouseEntity {

	protected LighthouseModifier() {
		this("");
	}

	public LighthouseModifier(String fqn) {
		super(fqn);
	}

}
