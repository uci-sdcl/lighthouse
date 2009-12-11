package edu.uci.lighthouse.model.repository;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import edu.uci.lighthouse.model.LighthouseAuthor;

@Entity
public class LighthouseRepositoryEvent {

	@SequenceGenerator(name="RepositoryEvent_Gen", sequenceName="RepositoryEvent_Gen")
	@Id @GeneratedValue(generator="RepositoryEvent_Gen")
	private Integer id;

	@OneToOne(cascade = CascadeType.ALL)
	private LighthouseAuthor author;

	private String className;

	@Temporal(TemporalType.TIMESTAMP)
	private Date eventTime;

	private Long versionAffected;

	public enum TYPE {
		CHECKIN, CHECKOUT, UPDATE, REVERT
	};
	private TYPE type;

	
	public LighthouseRepositoryEvent() {

	}
	
	public LighthouseRepositoryEvent(LighthouseAuthor author, TYPE type,
			String className, Date eventTime, Long versionAffected) {
		super();
		this.author = author;
		this.type = type;
		this.className = className;
		this.eventTime = eventTime;
		this.versionAffected = versionAffected;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LighthouseAuthor getAuthor() {
		return author;
	}

	public void setAuthor(LighthouseAuthor author) {
		this.author = author;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public Long getVersionAffected() {
		return versionAffected;
	}

	public void setVersionAffected(Long versionAffected) {
		this.versionAffected = versionAffected;
	}

	@Override
	public String toString() {
		return "LighthouseRepositoryEvent [author=" + author + ", className="
				+ className + ", eventTime=" + eventTime + ", type=" + type
				+ ", versionAffected=" + versionAffected + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result
				+ ((eventTime == null) ? 0 : eventTime.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result
				+ ((versionAffected == null) ? 0 : versionAffected.hashCode());
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
		LighthouseRepositoryEvent other = (LighthouseRepositoryEvent) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (eventTime == null) {
			if (other.eventTime != null)
				return false;
		} else if (!eventTime.equals(other.eventTime))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (versionAffected == null) {
			if (other.versionAffected != null)
				return false;
		} else if (!versionAffected.equals(other.versionAffected))
			return false;
		return true;
	}
	
}
