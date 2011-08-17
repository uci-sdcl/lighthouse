package edu.uci.lighthouse.lighthouseqandathreads.model;

import java.util.ArrayList;
import java.util.List;

public class Post {
	private String subject;
	private boolean question;
	private ArrayList<Post> responses = new ArrayList<Post>();
	private String message = "";
	private TeamMember author;
	
	public Post(boolean isQuestion, String message, TeamMember author){
		this.question = isQuestion;
		this.message = message;
		this.author = author;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setAuthor(TeamMember author) {
		this.author = author;
	}
	public TeamMember getTeamMemberAuthor() {
		return author;
	}
	public void setQuestion(boolean question) {
		this.question = question;
	}
	public boolean isQuestion() {
		return question;
	}
	
	public List<Post> getResponses(){
		return responses;
	}
	
	public void addResponse(Post post){
		responses.add(post);
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
	}
}