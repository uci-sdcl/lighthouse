package edu.uci.lighthouse.model.expertise;

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
import edu.uci.lighthouse.model.LighthouseClass;

//created by Alex Taubman
@Entity
public class LighthouseQuestion {

	
	@SequenceGenerator(name="Question_Gen", sequenceName="Question_Gen_Gen")
	@Id @GeneratedValue(generator="Question_Gent_Gen")
	private Integer id;


	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
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

	public LighthouseClass getLhClass() {
		return lhClass;
	}

	public void setLhClass(LighthouseClass lhClass) {
		this.lhClass = lhClass;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@OneToOne(cascade = CascadeType.ALL)
	private LighthouseAuthor author = null;
	
	@OneToOne(cascade = CascadeType.ALL)
	private LighthouseClass lhClass = null;

	private String subject = "";
	private String text = "";
	
	public Date getTimestamp() 
	{
		return timestamp;
	}

	public void setTimestamp(Date timestamp) 
	{
		this.timestamp = timestamp;
		
		
	}
	
	/*
	 * attributes:
	 * LHAuthor
	 * subject
	 * text
	 * LHClass
	 * time stamp
	 */
}
