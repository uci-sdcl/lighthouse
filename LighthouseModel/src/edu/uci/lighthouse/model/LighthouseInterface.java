package edu.uci.lighthouse.model;

import javax.persistence.Entity;

@Entity
public class LighthouseInterface extends LighthouseEntity {

	private static final long serialVersionUID = -2436541420470861250L;

	protected LighthouseInterface() {
		this("");
	}

	public LighthouseInterface(String fqn) {
		super(fqn);
	}

}
