package edu.uci.lighthouse.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Select the REL.FROM entity, by using REL.TYPE and REL.TO
 * */
@NamedQueries ({
	@NamedQuery(name = "LighthouseRelationship.findFromEntityByTypeAndToEntity",
				query = "SELECT rel.primaryKey.from " + 
						"FROM LighthouseRelationship rel " + 
						"WHERE rel.primaryKey.type = :relType " +
						"AND rel.primaryKey.to = :toEntity")
})

/**
 * Represents a relationship between 2 entities.
 */
@Entity
public class LighthouseRelationship implements Serializable{

	private static final long serialVersionUID = -2284456401070187413L;
	
	@EmbeddedId
	private LHRelationshipPK primaryKey;
	
	/** */
	public static enum TYPE {
		INSIDE, EXTENDS, IMPLEMENTS, RETURN, RECEIVES, HOLDS, USES, CALL, ACCESS, THROW, MODIFIED_BY
	}

	/**
	 * Creates a relationship between FROM and TO entities.
	 * 
	 * @param from
	 *            {@link LighthouseEntity}
	 * @param to
	 *            {@link LighthouseEntity}
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
	 * Returns the FROM entity of the relationship.
	 * 
	 * @return the FROM <code>Entity</code> instance
	 */
	public LighthouseEntity getFromEntity() {
		return this.primaryKey.getFrom();
	}

	/**
	 * Sets the FROM entity of the relationship.
	 * 
	 * @param from
	 *            an <code>Entity</code> instance
	 */
	public void setFromEntity(LighthouseEntity from) {
		this.primaryKey.setFrom(from);
	}

	/**
	 * Returns the TO entity of the relationship.
	 * 
	 * @return the TO <code>Entity</code> instance
	 */
	public LighthouseEntity getToEntity() {
		return this.primaryKey.getTo();
	}

	/**
	 * Sets the FROM entity of the relationship.
	 * 
	 * @param to
	 *            an <code>Entity</code> instance
	 */
	public void setToEntity(LighthouseEntity to) {
		this.primaryKey.setTo(to);
	}

	/**
	 * Returns the TYPE of the relationship.
	 * 
	 * @return {@link LighthouseRelationship.TYPE}
	 */
	public TYPE getType() {
		return this.primaryKey.getType();
	}

	/**
	 * Sets the TYPE of the relationship.
	 * 
	 * @param type
	 *            {@link LighthouseRelationship.TYPE}
	 */
	public void setType(TYPE type) {
		this.primaryKey.setType(type);
	}

	/**
	 * @return FROM + TYPE + TO
	 * */
	@Override
	public String toString() {
		String from = this.primaryKey.getFrom().getFullyQualifiedName();
		TYPE typeOfRelationship = this.primaryKey.getType();
		String to = this.primaryKey.getTo().getFullyQualifiedName();
		return " " + from + " " + typeOfRelationship + " " + to;
	}

	/**
	 * @return TO + TYPE + FROM
	 * */
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
