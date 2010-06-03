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

	@SequenceGenerator(name = "Question_Gen", sequenceName = "Question_Gen")
	@Id @GeneratedValue(generator = "Question_Gen")
	private Integer id;

	@OneToOne(cascade = CascadeType.ALL)
	private LighthouseAuthor author;

	@OneToOne(cascade = CascadeType.ALL)
	private LighthouseClass lhClass;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	private String subject;
	private String text;

	public LighthouseQuestion() {
		
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setAuthor(LighthouseAuthor author) {
		this.author = author;
	}

	public LighthouseAuthor getAuthor() {
		return author;
	}

	public void setLhClass(LighthouseClass lhClass) {
		this.lhClass = lhClass;
	}

	public LighthouseClass getLhClass() {
		return lhClass;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
	

}
