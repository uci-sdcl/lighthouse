package edu.uci.lighthouse.model.QAforums;

import java.io.Serializable;
import java.util.Vector;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import edu.uci.lighthouse.model.LighthouseAuthor;

@Entity
public class LHthreadCreator extends TeamMember implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4647444857832804956L;
	
	@Id
    @GeneratedValue
    int id;

	public LHthreadCreator(LighthouseAuthor author){
		super(author);
	}
	
	public void resolveThread(Post answer) {
		throw new UnsupportedOperationException();
	}

	public void startThread(Post post) {
		throw new UnsupportedOperationException();
	}
}