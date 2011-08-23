package edu.uci.lighthouse.lighthouseqandathreads.model;

import java.io.Serializable;
import java.util.Vector;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Solution implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8237475573760634640L;
	@OneToOne
	private Post question;
	@OneToOne
	private Post answer;
	
	
	public void setQuestion(Post question) {
		this.question = question;
	}
	public Post getQuestion() {
		return question;
	}
	public void setAnswer(Post answer) {
		this.answer = answer;
	}
	public Post getAnswer() {
		return answer;
	}
}