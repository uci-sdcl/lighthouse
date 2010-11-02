package edu.uci.lighthouse.model;

import javax.persistence.Entity;

@Entity
public class LighthouseMethod extends LighthouseEntity {

	private static final long serialVersionUID = 6710726246575035306L;

	protected LighthouseMethod() {
		this("");
	}

	public LighthouseMethod(String fqn) {
		super(fqn);
	}

}
