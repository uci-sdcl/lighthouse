package edu.uci.lighthouse.model;

import javax.persistence.Entity;

@Entity
public class LighthouseModifier extends LighthouseEntity {

	private static final long serialVersionUID = 1881434924787366382L;

	protected LighthouseModifier() {
		this("");
	}

	public LighthouseModifier(String fqn) {
		super(fqn);
	}

}
