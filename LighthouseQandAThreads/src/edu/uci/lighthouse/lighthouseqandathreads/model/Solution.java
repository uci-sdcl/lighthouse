package edu.uci.lighthouse.lighthouseqandathreads.model;

import java.util.Vector;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Solution {
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