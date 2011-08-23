package edu.uci.lighthouse.model.QAforums;

import java.io.Serializable;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Solution implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8237475573760634640L;
	
	@Id
    @GeneratedValue
    int id;
	
	@OneToOne (cascade = CascadeType.ALL)
	private Post question;
	@OneToOne (cascade = CascadeType.ALL)
	private Post answer;
	
	public Solution(){}
	
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