package edu.uci.lighthouse.model.QAforums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import edu.uci.lighthouse.LHmodelExtensions.LHclassPluginExtension;

@Entity
public class Post implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8635902581163644158L;

	@Id
    @GeneratedValue
    int id;
    
	private String subject;
	private boolean question;
	@OneToMany
	private ArrayList<Post> responses = new ArrayList<Post>();
	private String message = "";
	@ManyToOne
	private TeamMember author;
	
	public Post(boolean isQuestion, String subject, String message, TeamMember author){
		this.subject = subject;
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