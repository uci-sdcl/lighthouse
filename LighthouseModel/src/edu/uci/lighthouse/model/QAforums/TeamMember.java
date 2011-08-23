package edu.uci.lighthouse.model.QAforums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	
	@Id
    @GeneratedValue
    int id;
	
	 @OneToMany (cascade = CascadeType.ALL)
	Collection<Post> posts = new ArrayList<Post>();
	
	 public TeamMember(){}
	 
	public TeamMember(LighthouseAuthor author){
		this.setAuthor(author);
	}
	
	public void respondToThread(ForumThread aThread) {
		throw new UnsupportedOperationException();
	}

	public void setAuthor(LighthouseAuthor author) {
		this.author = author;
	}

	public LighthouseAuthor getAuthor() {
		return author;
	}
}