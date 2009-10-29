package edu.uci.lighthouse.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

/**
 * Represents a relationship between two entities.
 * 
 * @author tproenca
 * 
 */

@Entity
public class LighthouseRelationship {

	@EmbeddedId
	LHRelationshipPK primaryKey;
	
	/** */
	public static enum TYPE {
		INSIDE, EXTENDS, IMPLEMENTS, RETURN, RECEIVES, HOLDS, USES, CALL, ACCESS, THROW, MODIFIED_BY
	}

	/**
	 * Creates a relationship between from and to entities.
	 * 
	 * @param from
	 *            an entity
	 * @param to
	 *            other entity
	 */
	public LighthouseRelationship(LighthouseEntity from, LighthouseEntity to,
			TYPE type) {
		this.primaryKey = new LHRelationshipPK(from, to, type);
	}
	
	protected LighthouseRelationship() {
	}

	public LHRelationshipPK getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(LHRelationshipPK primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	/**
	 * Returns the from entity of the relationship.
	 * 
	 * @return the from <code>Entity</code> instance
	 */
	public LighthouseEntity getFromEntity() {
		return this.primaryKey.getFrom();
	}

	/**
	 * Sets the from entity of the relationship.
	 * 
	 * @param from
	 *            an <code>Entity</code> instance
	 */
	public void setFromEntity(LighthouseEntity from) {
		this.primaryKey.setFrom(from);
	}

	/**
	 * Returns the to entity of the relationship.
	 * 
	 * @return the to <code>Entity</code> instance
	 */
	public LighthouseEntity getToEntity() {
		return this.primaryKey.getTo();
	}

	/**
	 * Sets the from entity of the relationship.
	 * 
	 * @param to
	 *            an <code>Entity</code> instance
	 */
	public void setToEntity(LighthouseEntity to) {
		this.primaryKey.setTo(to);
	}

	/**
	 * Returns the from entity of the relationship.
	 * 
	 * @return the from <code>Entity</code> instance
	 */
	public TYPE getType() {
		return this.primaryKey.getType();
	}

	/**
	 * Sets the from entity of the relationship.
	 * 
	 * @param from
	 *            an <code>Entity</code> instance
	 */
	public void setType(TYPE type) {
		this.primaryKey.setType(type);
	}

	@Override
	public String toString() {
		String from = this.primaryKey.getFrom().getFullyQualifiedName();
		TYPE typeOfRelationship = this.primaryKey.getType();
		String to = this.primaryKey.getTo().getFullyQualifiedName();
		return " " + from + " " + typeOfRelationship + " " + to;
	}

	public String toStringInverse() {
		String from = this.primaryKey.getFrom().getFullyQualifiedName();
		TYPE typeOfRelationship = this.getType();
		String to = this.getToEntity().getFullyQualifiedName();
		return "\t\t " + to + " " + typeOfRelationship + " " + from;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((primaryKey == null) ? 0 : primaryKey.hashCode());
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
		LighthouseRelationship other = (LighthouseRelationship) obj;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		return true;
	}

}
