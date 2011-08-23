package edu.uci.lighthouse.lighthouseqandathreads.model;

import java.io.Serializable;
import java.util.Vector;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import edu.uci.lighthouse.model.LighthouseAuthor;

@Entity
public class TeamMember implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1320293063793751427L;
	@OneToOne
	private LighthouseAuthor author;
	
	public TeamMember(LighthouseAuthor author){
		this.setAuthor(author);
	}
	
	public void respondToThread(Thread aThread) {
		throw new UnsupportedOperationException();
	}

	public void setAuthor(LighthouseAuthor author) {
		this.author = author;
	}

	public LighthouseAuthor getAuthor() {
		return author;
	}
}