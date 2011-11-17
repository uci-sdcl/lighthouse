package edu.uci.lighthouse.extensions.codereview.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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

	@OneToOne(cascade = CascadeType.ALL)
	private LighthouseAuthor author;

	@Id @Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	private String content;
	
	public LighthouseAuthor getAuthor() {
		return author;
	}

	public void setAuthor(LighthouseAuthor author) {
		this.author = author;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
