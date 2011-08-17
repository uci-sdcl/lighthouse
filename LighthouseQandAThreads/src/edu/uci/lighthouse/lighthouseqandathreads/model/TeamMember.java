package edu.uci.lighthouse.lighthouseqandathreads.model;

import java.util.Vector;

import edu.uci.lighthouse.model.LighthouseAuthor;

public class TeamMember{
	private Vector<Post> posts = new Vector<Post>();
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