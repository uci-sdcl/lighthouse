package edu.uci.lighthouse.model;

import javax.persistence.Entity;

@Entity
public class LighthouseExternalClass extends LighthouseEntity {

	private static final long serialVersionUID = 7060552324299035047L;

	protected LighthouseExternalClass() {
		this("");
	}
	
	public LighthouseExternalClass(String fqn) {
		super(fqn);
	}

}
