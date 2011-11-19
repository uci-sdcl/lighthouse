package edu.uci.lighthouse.extensions.codereview.model;

import java.util.Date;

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

@Entity(name = "LighthouseComment")
public class Comment {

	@OneToOne
	private LighthouseAuthor author;

	@Id
	@GeneratedValue
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date timestamp;

	private String content;

	public Comment(LighthouseAuthor author, String content) {
		this.author = author;
		this.content = content;
	}

	protected Comment() {
		// Required by JPA
	}

	public Integer getId() {
		return id;
	}

	protected void setId(Integer id) {
		this.id = id;
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

	protected void setContent(String content) {
		this.content = content;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Comment) {
			Comment other = (Comment) obj;
			if (id.intValue() == other.id.intValue()
					&& author.equals(other.author)
					&& timestamp.equals(other.timestamp)
					&& content.equals(content)) {
				return true;
			}
		}
		return false;
	}
}
