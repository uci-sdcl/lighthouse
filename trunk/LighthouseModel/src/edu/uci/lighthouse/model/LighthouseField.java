package edu.uci.lighthouse.model;

import javax.persistence.Entity;

@Entity
public class LighthouseField extends LighthouseEntity {

	private static final long serialVersionUID = 6290735567708481729L;

	protected LighthouseField() {
		this("");
	}

	public LighthouseField(String fqn) {
		super(fqn);
	}

}
