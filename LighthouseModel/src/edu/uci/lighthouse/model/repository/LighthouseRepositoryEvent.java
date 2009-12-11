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

	public enum TYPE {
		CHECKIN, CHECKOUT, UPDATE, REVERT
	};

	@SequenceGenerator(name="RepositoryEvent_Gen", sequenceName="RepositoryEvent_Gen")
	@Id @GeneratedValue(generator="RepositoryEvent_Gen")
	private Integer id;

	@OneToOne(cascade = CascadeType.ALL)
	private LighthouseAuthor author;

	private TYPE type;

	private String className;

	@Temporal(TemporalType.TIMESTAMP)
	private Date eventTime;

	private Long versionAffected;

	public LighthouseRepositoryEvent() {

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

}
