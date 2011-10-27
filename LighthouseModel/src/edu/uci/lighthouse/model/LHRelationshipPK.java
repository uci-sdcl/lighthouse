/*******************************************************************************
* Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
*				, University of California, Irvine}.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    {Software Design and Collaboration Laboratory (SDCL)
*	, University of California, Irvine}
*			- initial API and implementation and/or initial documentation
*******************************************************************************/
package edu.uci.lighthouse.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

import edu.uci.lighthouse.model.LighthouseRelationship.TYPE;

/**
 * It is the primary key of LighthouseRelationship class
 * */
@Embeddable
public class LHRelationshipPK implements Serializable {

	private static final long serialVersionUID = -6866797359642111841L;

	@OneToOne(cascade = CascadeType.ALL)
	private LighthouseEntity from;

	@OneToOne(cascade = CascadeType.ALL)
	private LighthouseEntity to;

	private TYPE type;

	public LHRelationshipPK() {
	}

	public LHRelationshipPK(LighthouseEntity from, LighthouseEntity to,
			TYPE type) {
		this.from = from;
		this.to = to;
		this.type = type;
	}

	public LighthouseEntity getFrom() {
		return from;
	}

	public void setFrom(LighthouseEntity from) {
		this.from = from;
	}

	public LighthouseEntity getTo() {
		return to;
	}

	public void setTo(LighthouseEntity to) {
		this.to = to;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LHRelationshipPK other = (LHRelationshipPK) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
