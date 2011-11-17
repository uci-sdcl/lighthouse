package edu.uci.lighthouse.extensions.codereview.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import edu.uci.lighthouse.model.LighthouseAuthor;

/**
 * 
 * @author Tiago Proenca (tproenca@gmail.com)
 *
 */

@Entity(name="LighthouseComment")
public class Comment {

	@OneToOne
	private LighthouseAuthor author;

	@Id @GeneratedValue
	private Integer id;
	
	public Integer getId() {
		return id;
	}

	protected void setId(Integer id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date timestamp;

	private String content;
	
	public Comment(LighthouseAuthor author, String content){
		this.author = author;
		this.content = content;
	}
	
	protected Comment(){
		// Required by JPA
	}
	
	public LighthouseAuthor getAuthor() {
		return author;
	}

	protected void setAuthor(LighthouseAuthor author) {
		this.author = author;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	protected void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
